package io.github.excu101.vortexfilemanager.ui.view.additional

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.fileAdditionalActionIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileAdditionalSurfaceColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileAdditionalTitleTextColorKey
import io.github.excu101.vortexfilemanager.ui.util.appDuration

@Composable
fun AdditionalMenuHost(
    modifier: Modifier = Modifier,
    controller: AdditionalMenuController,
    onIconCloseClick: () -> Unit = { controller.hide() },
    enter: EnterTransition = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
    ),
    exit: ExitTransition = slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = 100, easing = FastOutLinearInEasing)
    ),
    content: @Composable AnimatedVisibilityScope.(String, () -> Unit, Collection<Action>) -> Unit = { menuMessage, listener, menuActions ->
        AdditionalMenu(
            text = menuMessage,
            onIconCloseClick = listener,
            actions = menuActions
        )
    },
) = AnimatedVisibility(
    modifier = modifier,
    visible = controller.state == AdditionalMenuController.State.OPENED,
    enter = enter,
    exit = exit,
    content = { content(controller.message, onIconCloseClick, controller.actions) }
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdditionalMenu(
    modifier: Modifier = Modifier,
    text: String,
    animateTextChanges: Boolean = true,
    iconClose: ImageVector = Icons.Outlined.Close,
    onIconCloseClick: () -> Unit,
    actions: Collection<Action>,
    onActionClick: (Action) -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = Theme[fileAdditionalSurfaceColorKey],
        elevation = 16.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .heightIn(min = 48.dp)
        ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onIconCloseClick,
                    indication = rememberRipple(
                        bounded = false
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                ),
                imageVector = iconClose,
                tint = Theme[fileAdditionalActionIconTintColorKey],
                contentDescription = "Close additional menu"
            )
            Spacer(modifier = Modifier.width(24.dp))
            if (animateTextChanges) {
                AnimatedContent(
                    targetState = text,
                    transitionSpec = {
                        slideInVertically(
                            tween(durationMillis = appDuration)
                        ) {
                            it
                        } + fadeIn(tween(durationMillis = appDuration)) with slideOutVertically(
                            tween(durationMillis = appDuration)
                        ) {
                            -it
                        } + fadeOut(tween(durationMillis = appDuration)) using SizeTransform(clip = false)
                    },
                    content = {
                        Text(
                            text = text,
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Theme[fileAdditionalTitleTextColorKey]
                            )
                        )
                    }
                )
            } else {
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }
            Spacer(modifier = Modifier.weight(weight = 1F, fill = true))
            actions.forEachIndexed { index, action ->
                Icon(
                    modifier = Modifier.clickable(
                        onClick = { onActionClick(action) },
                        indication = rememberRipple(
                            bounded = false
                        ),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = Theme[fileAdditionalActionIconTintColorKey]
                )
                if (index != actions.size - 1)
                    Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}