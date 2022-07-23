package io.github.excu101.vortexfilemanager.base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.ContainerHandler

@Composable
fun <S, E> ContainerHandler<S, E>.collectAsState(initial: S = container.state.value) =
    container.collectAsState(initial)

@Composable
fun <S, E> Container<S, E>.collectAsState(initial: S = state.value) = state.collectAsState(initial)

@Composable
fun <S, E> ContainerHandler<S, E>.collectAsEffect(initial: E) =
    container.collectAsEffect(initial = initial)

@Composable
fun <S, E> Container<S, E>.collectAsEffect(initial: E) = effect.collectAsState(initial = initial)