package io.github.excu101.vortexfilemanager.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
class BottomBarController(
    initialText: String?,
    initialActions: MutableList<Pair<ImageVector, () -> Unit>>
) {
    var previousText: String? = null
        private set

    var previousActions: SnapshotStateList<Pair<ImageVector, () -> Unit>> = mutableStateListOf()
        private set

    fun backToPrevious() {
        text = previousText
        actions = previousActions
    }

    var text: String? = mutableStateOf(initialText).value
        set(value) {
            previousText = field
            field = value
        }

    var actions: SnapshotStateList<Pair<ImageVector, () -> Unit>> = mutableStateListOf(
        elements = initialActions.toTypedArray()
    )
        set(value) {
            previousActions = value
            field = value
        }
}

@Composable
fun rememberBottomBarController(
    text: String? = null,
    actions: MutableList<Pair<ImageVector, () -> Unit>> = mutableListOf(
        Icons.Outlined.Menu to {

        },
        Icons.Outlined.Search to {

        },
        Icons.Outlined.MoreVert to {

        }
    )
) = remember(text, actions) {
    BottomBarController(text, actions)
}