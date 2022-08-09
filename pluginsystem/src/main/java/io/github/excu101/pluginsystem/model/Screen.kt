package io.github.excu101.pluginsystem.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

interface Screen {

    val route: String

    val icon: ImageVector?

    val content: @Composable () -> Unit

}