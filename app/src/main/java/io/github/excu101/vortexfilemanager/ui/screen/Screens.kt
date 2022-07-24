package io.github.excu101.vortexfilemanager.ui.screen

sealed class Screen(val route: String) {

    object Storage : Screen(route = "storage")
    object FileInfo : Screen(route = "fileInfo") {
        const val args: String = "filePath"
    }

    object Plugin : Screen(route = "plugin")
    object Settings : Screen(route = "settings")

}