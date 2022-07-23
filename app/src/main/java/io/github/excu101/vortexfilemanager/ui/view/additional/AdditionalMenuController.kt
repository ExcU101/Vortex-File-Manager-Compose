package io.github.excu101.vortexfilemanager.ui.view.additional

import androidx.compose.runtime.*
import io.github.excu101.pluginsystem.model.Action

interface MenuData {
    val message: String
    val actions: List<Action>
}

internal class MenuDataImpl(
    override val message: String,
    override val actions: List<Action> = listOf(),
) : MenuData

class AdditionalMenuController(
    state: State = State.CLOSED,
) {
    enum class State {
        CLOSED,
        OPENED
    }

    var state by mutableStateOf(value = state)
        private set

    var message by mutableStateOf("")

    var actions: Collection<Action> by mutableStateOf(listOf())

    fun hide() {
        state = State.CLOSED
    }

    fun show() {
        state = State.OPENED
    }

}

@Composable
fun rememberAdditionalMenuController(
    state: AdditionalMenuController.State = AdditionalMenuController.State.CLOSED,
) = remember() {
    AdditionalMenuController(state = state)
}