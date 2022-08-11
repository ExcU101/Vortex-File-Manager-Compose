package io.github.excu101.vortexfilemanager.ui.screen.main.view

import androidx.compose.material.BottomDrawer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.provider.Managers
import io.github.excu101.ui.component.layout.SelectableSectionList
import io.github.excu101.ui.component.utils.Content
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.mainDrawerSurfaceColorKey
import io.github.excu101.vortexfilemanager.util.drawerColors
import io.github.excu101.vortexfilemanager.util.item
import io.github.excu101.vortexfilemanager.util.listBuilder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomDrawer(
    modifier: Modifier = Modifier,
    controller: MainScreenController,
    selectedAction: Action,
    content: Content,
) {
    val notify: (Action) -> Unit = remember(controller) { (controller::notifyActionListeners) }

    BottomDrawer(
        modifier = modifier,
        drawerState = controller.drawer,
        drawerBackgroundColor = Theme[mainDrawerSurfaceColorKey],
        gesturesEnabled = controller.isDrawerVisible,
        drawerContent = {
            SelectableSectionList(
                colors = drawerColors(),
                sections = controller.menu + Managers.Screen.screens.map { (plugin, screen) ->
                    GroupAction(
                        name = plugin.attributes.name,
                        icon = null,
                        actions = listBuilder {
                            screen.icon?.let { item(title = screen.route, icon = it) }
                        }
                    )
                },
                onActionClick = notify,
                selectedActions = listOf(selectedAction)
            )
        },
        content = content,
    )
}