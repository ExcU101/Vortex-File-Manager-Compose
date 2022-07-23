package io.github.excu101.vortexfilemanager.ui.view.action

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.excu101.pluginsystem.model.Action

val StorageAction = Action(title = "Storage", icon = Icons.Outlined.Folder)
val PluginsAction = Action(title = "Plugin manager", icon = Icons.Outlined.AddCircleOutline)
val SettingsAction = Action(title = "Settings", icon = Icons.Outlined.Settings)
val MenuAction = Action(title = "Menu", icon = Icons.Outlined.Menu)

val CreateFileAction = Action(title = "Add new file", icon = Icons.Outlined.Add)

@Composable
fun ActionItem(
    action: Action,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .sizeIn(minHeight = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = action.icon, contentDescription = action.title)
            Spacer(modifier = Modifier.width(32.dp))
            Text(text = action.title)
        }
    }
}