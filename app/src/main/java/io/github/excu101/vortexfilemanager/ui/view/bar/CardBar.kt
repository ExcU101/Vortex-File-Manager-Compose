package io.github.excu101.vortexfilemanager.ui.view.bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarActionIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarNavigationIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarTitleTextColorKey
import io.github.excu101.vortexfilemanager.ui.view.action.MenuAction

@Composable
fun CardBar(
    modifier: Modifier = Modifier,
    onNavigationIconClick: (Action) -> Unit,
    value: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    actions: Collection<Action>,
    onActionClick: (Action) -> Unit
) {
    Surface(
        modifier = modifier.padding(16.dp),
        elevation = 8.dp,
        color = Theme[mainBarSurfaceColorKey],
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = MenuAction.icon,
                tint = Theme { mainBarNavigationIconTintColorKey },
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onNavigationIconClick(MenuAction)
                    },
                    indication = rememberRipple(
                        bounded = false
                    )
                ),
                contentDescription = MenuAction.title
            )
            Spacer(modifier = Modifier.width(32.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChanged,
                singleLine = true,
                maxLines = 1,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Theme[mainBarTitleTextColorKey]
                ),
                cursorBrush = SolidColor(Theme[mainBarTitleTextColorKey])
            )
            Spacer(modifier = Modifier.weight(weight = 1F, fill = true))
            actions.forEachIndexed { index, action ->
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = Theme { mainBarActionIconTintColorKey },
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onActionClick(action)
                        },
                        indication = rememberRipple(
                            bounded = false
                        )
                    ),
                )
                if (actions.size - 1 != index)
                    Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}