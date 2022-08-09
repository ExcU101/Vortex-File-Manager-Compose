package io.github.excu101.vortexfilemanager.base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.excu101.vortexfilemanager.base.Container
import io.github.excu101.vortexfilemanager.base.ContainerHandler
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <S, E> ContainerHandler<S, E>.collectAsState(
    initial: S = container.state.value,
    context: CoroutineContext = EmptyCoroutineContext,
) = container.collectAsState(
    initial = initial,
    context = context
)

@Composable
fun <S, E> Container<S, E>.collectAsState(
    initial: S = state.value,
    context: CoroutineContext = EmptyCoroutineContext
) = state.collectAsState(
    initial = initial,
    context = context
)

@Composable
fun <S, E> ContainerHandler<S, E>.collectAsEffect(
    initial: E,
    context: CoroutineContext = EmptyCoroutineContext,
) = container.collectAsEffect(
    initial = initial,
    context = context
)

@Composable
fun <S, E> Container<S, E>.collectAsEffect(
    initial: E,
    context: CoroutineContext = EmptyCoroutineContext,
) = effect.collectAsState(
    initial = initial,
    context = context
)