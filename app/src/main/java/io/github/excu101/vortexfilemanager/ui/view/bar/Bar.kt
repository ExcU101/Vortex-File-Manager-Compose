package io.github.excu101.vortexfilemanager.ui.view.bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortexfilemanager.ui.view.action.MenuAction

@Immutable
class BarController(
    initRequest: String = "",
    initNavigationAction: Action = MenuAction,
    initActions: Collection<Action> = listOf(),
    initIsContextual: Boolean = false,
) {

    var isHide = mutableStateOf(value = false)

    var request = mutableStateOf(value = initRequest)

    var navigationAction = mutableStateOf(value = initNavigationAction)

    var actions = mutableStateOf(value = initActions)

    var isContextual = mutableStateOf(value = initIsContextual)

}
@Composable
fun rememberBarController(
    initRequest: String = "",
    initNavigationAction: Action = MenuAction,
    initActions: Collection<Action> = listOf(),
    initIsContextual: Boolean = false,
) = remember(
    initRequest,
    initActions,
    initIsContextual,
) {
    BarController(
        initRequest = initRequest,
        initNavigationAction = initNavigationAction,
        initActions = initActions,
        initIsContextual = initIsContextual
    )
}