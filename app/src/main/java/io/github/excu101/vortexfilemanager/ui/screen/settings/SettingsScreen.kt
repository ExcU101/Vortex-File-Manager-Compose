package io.github.excu101.vortexfilemanager.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.backgroundColorKey
import io.github.excu101.vortexfilemanager.ui.view.action.ActionItem
import io.github.excu101.vortexfilemanager.util.item
import io.github.excu101.vortexfilemanager.util.listBuilder

@Composable
fun SettingsScreen(
    actions: List<Action> = listBuilder {
        item(title = "Notifications", icon = Icons.Outlined.Notifications)
        item(title = "Appearance", icon = Icons.Outlined.Brush)
        item(title = "Report", icon = Icons.Outlined.BugReport)
    }
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Theme[backgroundColorKey]
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.align(Alignment.BottomCenter)) {
                itemsIndexed(actions) { index, item ->
                    ActionItem(
                        modifier = Modifier.clickable {

                        },
                        action = item
                    )
                }
            }
        }

    }

}