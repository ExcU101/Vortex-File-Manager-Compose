package io.github.excu101.vortexfilemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.excu101.vortexfilemanager.api.EventHandler
import io.github.excu101.vortexfilemanager.data.intent.Contracts
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E, SE> : ViewModel, EventHandler<E> {

    constructor()

    private val _state = MutableStateFlow(Contracts.State.Loading() as S)
    val state: StateFlow<S>
        get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<E>()
    val event: SharedFlow<E>
        get() = _event.asSharedFlow()

    private val _effect = Channel<SE>()
    val effect: Flow<SE>
        get() = _effect.receiveAsFlow()

    infix fun postEvent(event: E) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    protected fun postState(state: S.() -> S) {
        val newState = state(_state.value)
        _state.value = newState
    }

    protected fun postEffect(builder: () -> SE) {
        viewModelScope.launch { _effect.send(builder()) }
    }

}