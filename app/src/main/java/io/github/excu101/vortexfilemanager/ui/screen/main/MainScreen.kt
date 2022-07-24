package io.github.excu101.vortexfilemanager.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.github.excu101.vortexfilemanager.ui.rememberMainScreenController
import io.github.excu101.vortexfilemanager.ui.screen.Screen
import io.github.excu101.vortexfilemanager.ui.screen.list.StorageScreen
import io.github.excu101.vortexfilemanager.ui.screen.main.view.MainBottomDrawer
import io.github.excu101.vortexfilemanager.ui.screen.main.view.MainScaffold
import io.github.excu101.vortexfilemanager.ui.screen.plugin.PluginScreen
import io.github.excu101.vortexfilemanager.ui.screen.settings.SettingsScreen
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.defaultDarkTheme
import io.github.excu101.vortexfilemanager.ui.theme.defaultLightTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val uiController = rememberSystemUiController()
    val navigator = rememberNavController()
    val controller = rememberMainScreenController()
    var currentPos by remember {
        mutableStateOf(0)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        controller.subscribeOnAction(index = 0) {
            when (it.title) {
                "Menu" -> {
                    if (controller.isDrawerVisible) {
                        controller.hideDrawer(scope)
                    }
                    controller.toggleDrawer(scope)
                }
                "Storage" -> {
                    currentPos = 0
                    navigator.navigate(Screen.Storage.route)
                    controller.hideDrawer(scope)
                }
                "Plugin manager" -> {
                    currentPos = 1
                    navigator.navigate(Screen.Plugin.route)
                    controller.hideDrawer(scope)
                }
                "Settings" -> {
                    currentPos = 2
                    navigator.navigate(Screen.Settings.route)
                    controller.hideDrawer(scope)
                }
            }
        }
        onDispose {
            controller.unsubscribeOnAction(index = 0)
        }
    })

    val isDark = Theme.systemTheme.isDark
    SideEffect {
        if (isDark) {
            defaultDarkTheme()
        } else {
            defaultLightTheme()
        }
    }

    MainScaffold(
        controller = controller,
        modifier = Modifier.fillMaxSize(),
    ) { values ->
        MainBottomDrawer(
            controller = controller,
            currentScreenSelected = currentPos
        ) {
            NavHost(
                navController = navigator,
                startDestination = Screen.Storage.route
            ) {
                composable(
                    route = Screen.Storage.route,
                ) { entry ->
                    StorageScreen(
                        uiController = uiController,
                        navigator = navigator,
                        controller = controller,
                    )
                }

                composable(
                    route = Screen.Plugin.route
                ) { entry ->
                    PluginScreen()
                }

                composable(
                    route = Screen.Settings.route
                ) { entry ->
                    SettingsScreen()
                }
            }
        }
    }
}