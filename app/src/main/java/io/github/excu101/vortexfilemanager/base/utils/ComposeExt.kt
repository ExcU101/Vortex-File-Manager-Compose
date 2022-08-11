package io.github.excu101.vortexfilemanager.base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun <S, E> ContainerHandler<S, E>.collectAsStateWithLifecycle(
    initial: S = container.state.value,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
) = container.collectAsStateWithLifecycle(
    initial = initial,
    lifecycleOwner = lifecycleOwner,
    minActiveState = minActiveState,
    context = context
)

@Composable
fun <S, E> Container<S, E>.collectAsState(
    initial: S = state.value,
    context: CoroutineContext = EmptyCoroutineContext,
) = state.collectAsState(
    initial = initial,
    context = context
)

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun <S, E> Container<S, E>.collectAsStateWithLifecycle(
    initial: S = state.value,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
) = state.collectAsStateWithLifecycle(
    initialValue = initial,
    lifecycleOwner = lifecycleOwner,
    minActiveState = minActiveState,
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
fun <S, E> ContainerHandler<S, E>.collectAsEffectWithLifecycle(
    initial: E,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
) = container.collectAsEffectWithLifecycle(
    initial = initial,
    lifecycleOwner = lifecycleOwner,
    minActiveState = minActiveState,
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

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun <S, E> Container<S, E>.collectAsEffectWithLifecycle(
    initial: E,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
) = effect.collectAsStateWithLifecycle(
    initialValue = initial,
    lifecycleOwner = lifecycleOwner,
    minActiveState = minActiveState,
    context = context
)