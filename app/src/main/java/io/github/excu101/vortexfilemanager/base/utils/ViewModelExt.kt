package io.github.excu101.vortexfilemanager.base.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.ContainerHandler
import io.github.excu101.vortexfilemanager.base.impl.ContainerImpl

abstract class ViewModelContainerHandler<S, E>(initialState: S) : ViewModel(),
    ContainerHandler<S, E> {
    final override val container: Container<S, E> = container(initialState)
}

internal fun <S, E> ViewModel.container(initialState: S): Container<S, E> = ContainerImpl(
    initState = initialState,
    parentScope = viewModelScope
)