package io.github.excu101.vortexfilemanager.base.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.ContainerHandler
import io.github.excu101.vortexfilemanager.base.Logger
import io.github.excu101.vortexfilemanager.base.impl.ContainerImpl
import io.github.excu101.vortexfilemanager.base.impl.LoggerImpl
import kotlin.math.log

abstract class ViewModelContainerHandler<S, E>(initialState: S) : ViewModel(),
    ContainerHandler<S, E> {
    final override val container: Container<S, E> = container(initialState)
}

internal fun <S, E> ViewModel.container(
    initialState: S,
    tag: String = "Loggable"
): Container<S, E> = container(initialState = initialState, logger = LoggerImpl(tag = tag))

internal fun <S, E> ViewModel.container(
    initialState: S,
    logger: Logger
): Container<S, E> = ContainerImpl(
    initState = initialState,
    parentScope = viewModelScope,
    logger = logger
)

context (ContainerHandler<S, E>)
val <S, E> ViewModel.state
    get() = container.state.value