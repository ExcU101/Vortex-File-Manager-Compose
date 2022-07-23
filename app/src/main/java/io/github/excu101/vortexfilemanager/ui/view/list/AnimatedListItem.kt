package io.github.excu101.vortexfilemanager.ui.view.list

import androidx.compose.runtime.Composable

class AnimatedListItem<out T>(
    val key: Any? = null,
    val value: T,
    val content: @Composable () -> Unit,
)