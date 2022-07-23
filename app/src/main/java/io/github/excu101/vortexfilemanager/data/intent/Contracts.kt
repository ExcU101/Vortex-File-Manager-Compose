package io.github.excu101.vortexfilemanager.data.intent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortexfilemanager.base.utils.IntentScope
import io.github.excu101.vortexfilemanager.base.utils.reduce
import io.github.excu101.vortexfilemanager.base.utils.side
import io.github.excu101.vortexfilemanager.data.FileModelSet
import io.github.excu101.vortexfilemanager.util.listBuilder

object Contracts {

    @Immutable
    sealed class State {

        @Immutable
        class CriticalError(val error: Throwable) : State()

        @Immutable
        data class FileList(
            val data: FileModelSet,
            val actions: List<Action> = listBuilder { },
        ) : State()

        @Immutable
        class Loading(val text: String? = null) : State()

        @Immutable
        object RequiresPerm : State()

        @Immutable
        object RequiresFullStoragePerm : State()

        @Immutable
        class Warning(
            val message: String,
            val icon: ImageVector = Icons.Outlined.Info,
            val actions: List<Action> = listOf(),
        ) : State()
    }

    sealed class SideEffect {
        object Empty : SideEffect()

        object DialogEmpty : SideEffect()
        object DialogCreate : SideEffect()
        class DialogWarning(
            val message: String,
        ) : SideEffect()

        class ModelInfo : SideEffect() {
            override fun equals(other: Any?): Boolean {
                return this === other
            }

            override fun hashCode(): Int {
                return System.identityHashCode(this)
            }
        }

        class Message(
            val text: String,
            val labelAction: String? = null,
            val action: (() -> Unit)? = null,
        ) : SideEffect()
    }
}

suspend fun IntentScope<Contracts.State, Contracts.SideEffect>.loading(
    text: String? = null,
) {
    reduce { Contracts.State.Loading(text = text) }
}

suspend fun IntentScope<Contracts.State, Contracts.SideEffect>.error() {

}

suspend fun IntentScope<Contracts.State, Contracts.SideEffect>.message(
    text: String,
    labelAction: String? = null,
    action: (() -> Unit)? = null,
) {
    side(
        effect = Contracts.SideEffect.Message(
            text = text,
            labelAction = labelAction,
            action = action
        )
    )
}