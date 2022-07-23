package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.fileAdditionalActionIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileAdditionalSurfaceColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileAdditionalTitleTextColorKey

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FileAdditionalActionView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    text: String,
    icon: ImageVector,
    onSurfaceClick: () -> Unit = { },
    onIconClick: () -> Unit = { },
    onCloseClick: () -> Unit = { }
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 100, easing = FastOutLinearInEasing)
        ),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .clickable(onClick = onSurfaceClick),
            color = Theme[fileAdditionalTitleTextColorKey],
            elevation = 16.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 56.dp),
            ) {
                IconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onCloseClick,
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = Theme[fileAdditionalSurfaceColorKey]
                        )
                    }
                )
                AnimatedContent(
                    targetState = text,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() with slideOutVertically { height -> -height } + fadeOut() using
                                SizeTransform(false)
                    },
                    content = {
                        Text(
                            text = it,
                            style = TextStyle(
                                color = Theme[fileAdditionalTitleTextColorKey]
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.weight(1F, true))
                IconButton(
                    onClick = { },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowUp,
                            contentDescription = null,
                            tint = Theme[fileAdditionalActionIconTintColorKey]
                        )
                    }
                )
                IconButton(
                    onClick = onIconClick,
                    content = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Theme[fileAdditionalActionIconTintColorKey]
                        )
                    }
                )
            }
        }

    }
}