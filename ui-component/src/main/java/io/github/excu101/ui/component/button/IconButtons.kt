package io.github.excu101.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object IconButtons {
    object Outlined
}

@Composable
fun IconButtonContainer(
    vector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    Icon(
        modifier = modifier.clickable(
            onClick = onClick,
            indication = rememberRipple(
                bounded = false
            ),
            interactionSource = remember { MutableInteractionSource() }
        ),
        imageVector = vector,
        contentDescription = contentDescription,
        tint = tint
    )
}

@Composable
fun IconButtons.Outlined.Menu(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {}
) {
    IconButtonContainer(
        modifier = modifier,
        vector = Icons.Outlined.Menu,
        contentDescription = contentDescription,
        tint = tint,
        onClick = onClick
    )
}

@Composable
fun IconButtons.Outlined.Sort(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {}
) {
    IconButtonContainer(
        modifier = modifier,
        vector = Icons.Outlined.Sort,
        contentDescription = contentDescription,
        tint = tint,
        onClick = onClick
    )
}


@Composable
fun IconButtons.Outlined.MoreVert(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit = {}
) {
    IconButtonContainer(
        modifier = modifier,
        vector = Icons.Outlined.MoreVert,
        contentDescription = contentDescription,
        tint = tint,
        onClick = onClick
    )
}