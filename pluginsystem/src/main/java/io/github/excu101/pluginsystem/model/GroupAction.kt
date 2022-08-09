package io.github.excu101.pluginsystem.model

import androidx.compose.ui.graphics.vector.ImageVector

data class GroupAction(
    val name: String,
    val icon: ImageVector? = null,
    val actions: Collection<Action> = listOf()
)