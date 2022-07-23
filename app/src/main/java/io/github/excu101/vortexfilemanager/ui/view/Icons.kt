package io.github.excu101.vortexfilemanager.ui.view

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ClickableIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) = IconButton(
    modifier = modifier,
    onClick = onClick,
    content = {
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = contentDescription
        )
    }
)