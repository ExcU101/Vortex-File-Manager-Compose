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
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.ui.menu.Menu
import io.github.excu101.vortexfilemanager.ui.menu.action
import io.github.excu101.vortexfilemanager.ui.menu.menu
import io.github.excu101.vortexfilemanager.ui.menu.section

fun Menu.asGroup(): List<GroupAction> {
    val list = mutableListOf<GroupAction>()
    sections.forEach { section ->
        list.add(
            GroupAction(
                section.title,
                section.icon,
                actions = section.actions.map { Action(title = it.title, icon = it.icon) })
        )
    }
    return list
}

fun Menu.asAction(): List<Action> {
    val list = mutableListOf<Action>()
    actions.forEach {
        list.add(Action(title = it.title, icon = it.icon))
    }
    return list
}


val StorageAction = Action(title = "Storage", icon = Icons.Outlined.Folder)
val PluginsAction = Action(title = "Plugin manager", icon = Icons.Outlined.AddCircleOutline)
val SettingsAction = Action(title = "Settings", icon = Icons.Outlined.Settings)
val BookmarkAction = Action(title = "Bookmarks", icon = Icons.Outlined.Bookmarks)
val MenuAction = Action(title = "Menu", icon = Icons.Outlined.Menu)

val CreateFileAction = Action(title = "Add new file", icon = Icons.Outlined.Add)

val Ordering = menu {
    section {
        title = "Sort"

        action {
            title = "Name"
        }
        action {
            title = "Last modifier time"
        }
        action {
            title = "Creation time"
        }
        action {
            title = "Last access time"
        }
        action {
            title = "Size"
        }
    }
    section {
        title = "Order"

        action {
            title = "A - Z"
        }
        action {
            title = "Z - A"
        }
    }
    section {
        title = "Filter"

        action {
            title = "Only files"
        }
        action {
            title = "Only folders"
        }
    }
    section {
        title = "View"

        action {
            title = "Grid"
        }
    }
}

fun Defaults(vararg selected: FileModel) = menu {
    section {
        title = "Operations"
        action {
            title = "Add new file"
            icon = Icons.Outlined.Add
        }
        action {
            title = "Delete"
            icon = Icons.Outlined.Delete
        }
    }
    section {
        title = ""
        if (selected.isEmpty()) {
            action {
                title = "Select all"
                icon = Icons.Outlined.SelectAll
            }
        } else {
            action {
                title = "Deselect all"
                icon = Icons.Outlined.Deselect
            }
        }

        action {
            title = "Info"
            icon = Icons.Outlined.Info
        }
    }
}

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