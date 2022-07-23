package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.fileWarningActionContentColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileWarningBackgroundColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileWarningIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileWarningTitleTextColorKey


@Composable
fun FileWarningView(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    actions: List<Action>,
    onActionClick: (Action) -> Unit
) {
    Column(
        modifier = modifier.background(color = Theme[fileWarningBackgroundColorKey]),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Theme[fileWarningIconTintColorKey]
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                color = Theme[fileWarningTitleTextColorKey],
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.size(16.dp))
        actions.forEach { action ->
            TextButton(
                onClick = { onActionClick(action) },
                colors = ButtonDefaults.textButtonColors(contentColor = Theme[fileWarningActionContentColorKey]),
                content = {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = action.title)
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun SpecialPermWarning(
    modifier: Modifier = Modifier,
    onActionClick: (Action) -> Unit
) = FileWarningView(
    modifier = modifier,
    icon = Icons.Outlined.Info,
    text = "App requires special permission for working with files, this perm gives app to work with Android file system",
    actions = listOf(
        Action(
            title = "Provide",
            icon = Icons.Outlined.Add,
        )
    ),
    onActionClick = onActionClick
)

@Composable
fun PermWarning(
    modifier: Modifier = Modifier,
    onActionClick: (Action) -> Unit
) = FileWarningView(
    modifier = modifier,
    icon = Icons.Outlined.Info,
    text = "App requires read-write permission for working with files, please grant perm or go to setting and do first step",
    actions = listOf(
        Action(
            title = "Provide",
            icon = Icons.Outlined.Add,
        )
    ),
    onActionClick = onActionClick
)
