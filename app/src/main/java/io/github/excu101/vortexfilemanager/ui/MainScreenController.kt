package io.github.excu101.vortexfilemanager.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.utils.group
import io.github.excu101.vortexfilemanager.data.intent.ActionListener
import io.github.excu101.vortexfilemanager.ui.view.action.BookmarkAction
import io.github.excu101.vortexfilemanager.ui.view.action.PluginsAction
import io.github.excu101.vortexfilemanager.ui.view.action.SettingsAction
import io.github.excu101.vortexfilemanager.ui.view.action.StorageAction
import io.github.excu101.vortexfilemanager.ui.view.bar.BarController
import io.github.excu101.vortexfilemanager.ui.view.bar.rememberBarController
import io.github.excu101.vortexfilemanager.ui.view.icon.MenuIconState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
@OptIn(ExperimentalMaterialApi::class)
class MainScreenController(
    initMenu: Collection<GroupAction>,
    val drawer: BottomDrawerState,
    val snackbar: SnackbarHostState,
    val bar: BarController,
) {

    private val _actionListeners = mutableListOf<ActionListener>()
    val actionListeners: List<ActionListener>
        get() = _actionListeners

    var menu: SnapshotStateList<GroupAction> =
        mutableStateListOf(elements = initMenu.toTypedArray())

    fun changeMenu(block: SnapshotStateList<GroupAction>.() -> Unit) {
        menu.apply(block)
    }

    var navigationIconState by mutableStateOf(MenuIconState.MENU)

    inline fun subscribeOnAction(crossinline block: (Action) -> Unit) {
        subscribeOnAction(ActionListener { action -> block(action) })
    }

    inline fun subscribeOnAction(index: Int, crossinline block: (Action) -> Unit) {
        subscribeOnAction(index, ActionListener { action -> block(action) })
    }

    fun subscribeOnAction(index: Int, listener: ActionListener) {
        _actionListeners.add(index = index, listener)
    }

    fun subscribeOnAction(listener: ActionListener) {
        _actionListeners.add(listener)
    }

    fun unsubscribeOnAction(listener: ActionListener) {
        _actionListeners.remove(listener)
    }

    fun unsubscribeOnAction(index: Int) {
        _actionListeners.removeAt(index = index)
    }

    fun notifyActionListeners(action: Action) {
        actionListeners.forEach { listener ->
            listener.onCall(action)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    val isDrawerVisible: Boolean
        get() = drawer.isOpen || drawer.isExpanded

    @OptIn(ExperimentalMaterialApi::class)
    suspend fun showDrawer() {
        navigationIconState = MenuIconState.CLOSE
        drawer.expand()
    }

    fun showDrawer(scope: CoroutineScope) {
        scope.launch { showDrawer() }
    }

    @OptIn(ExperimentalMaterialApi::class)
    suspend fun hideDrawer() {
        navigationIconState = MenuIconState.MENU
        drawer.close()
    }

    fun hideDrawer(scope: CoroutineScope) {
        scope.launch {
            hideDrawer()
        }
    }

    fun toggleDrawer(scope: CoroutineScope) {
        scope.launch {
            toggleDrawer()
        }
    }

    suspend fun toggleDrawer() = if (isDrawerVisible) {
        hideDrawer()
    } else {
        showDrawer()
    }

    suspend fun showSnackbar(
        message: String,
        actionText: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) = snackbar.showSnackbar(
        message = message,
        actionLabel = actionText,
        duration = duration
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberMainScreenController(
    initMenu: List<GroupAction> = listOf(
        group(title = "Main") {
            item(StorageAction)
            item(PluginsAction)
        },
        group(title = "Additional") {
            item(BookmarkAction)
            item(SettingsAction)
        }
    ),
    drawer: BottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed),
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    bar: BarController = rememberBarController()
) = remember(initMenu) {
    MainScreenController(
        initMenu = initMenu,
        snackbar = snackbarHostState,
        drawer = drawer,
        bar = bar,
    )
}