package io.github.excu101.vortexfilemanager.base.impl

import io.github.excu101.vortexfilemanager.base.Collector
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.utils.ContainerScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ContainerImpl<S, E>(
    initState: S,
    private val parentScope: CoroutineScope,
) : Container<S, E> {

    private val _effect = Channel<E>()
    private val _state = MutableStateFlow(initState)
    private val _collector = CollectorImpl<S>()

    override val collector: Collector<S>
        get() = _collector

    override val effect: Flow<E>
        get() = _effect.receiveAsFlow()

    override val state: StateFlow<S>
        get() = _state.asStateFlow()

    private val scope: ContainerScope<S, E> = ContainerScope(
        getState = { state.value },
        reduce = { reducer ->
            emitState(reducer(state.value))
        },
        effect = { effect ->
            emitEffect(effect)
        }
    )

    fun emitState(state: S) {
        parentScope.launch {
            _state.emit(state)
            _collector.emit(state)
        }
    }

    fun emitEffect(effect: E) {
        parentScope.launch { _effect.send(effect) }
    }

    override fun emit(block: suspend ContainerScope<S, E>.() -> Unit) {
        parentScope.launch(Unconfined) {
            scope.block()
        }
    }

}