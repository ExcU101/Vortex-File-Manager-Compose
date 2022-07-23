package io.github.excu101.vortexfilemanager.ui.theme

import io.github.excu101.vortexfilemanager.ui.theme.key.fileListDirectoriesCountKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileListFilesCountKey

fun defaultTexts() {
    Theme[fileListFilesCountKey] = "Files: %s"
    Theme[fileListDirectoriesCountKey] = "Folders: %s"
}