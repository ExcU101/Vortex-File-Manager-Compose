package io.github.excu101.vortexfilemanager.ui.screen.main.view

import androidx.compose.material.BottomDrawer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.mainDrawerSurfaceColorKey

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainBottomDrawer(
    modifier: Modifier = Modifier,
    controller: MainScreenController,
    currentScreenSelected: Int,
    content: @Composable () -> Unit
) {
    BottomDrawer(
        modifier = modifier,
        drawerState = controller.drawer,
        drawerBackgroundColor = Theme[mainDrawerSurfaceColorKey],
        gesturesEnabled = controller.isDrawerVisible,
        drawerContent = {
            MainNavigationMenu(
                navigationMenu = controller.menu,
                onActionClick = { action ->
                    controller.notifyActionListeners(action)
                },
                currentSelected = currentScreenSelected
            )
        },
        content = content,
    )
}