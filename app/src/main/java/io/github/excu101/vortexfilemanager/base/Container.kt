package io.github.excu101.vortexfilemanager.base

import io.github.excu101.vortexfilemanager.base.utils.ContainerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Container<S, E> {

    val effect: Flow<E>

    val state: StateFlow<S>

    val collector: Collector<S>

    fun emit(block: suspend ContainerScope<S, E>.() -> Unit)

}