package io.github.excu101.pluginsystem.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class Action(
    val title: String,
    val icon: ImageVector,
)