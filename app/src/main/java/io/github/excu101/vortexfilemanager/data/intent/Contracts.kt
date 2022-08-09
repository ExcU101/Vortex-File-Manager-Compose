package io.github.excu101.vortexfilemanager.data.intent

import androidx.compose.runtime.Immutable
import io.github.excu101.vortexfilemanager.base.utils.IntentScope
import io.github.excu101.vortexfilemanager.base.utils.side
import io.github.excu101.vortexfilemanager.data.FileModel

@Immutable
data class StorageState(
    val data: List<FileModel> = listOf(),
    val isLoading: Boolean = false,
    val loadingMessage: String = "",
    val error: Throwable? = null,
    val requiresPermission: Boolean = false,
    val requiresSpecialPermission: Boolean = false,
)

@Immutable
sealed class StorageDialogState {
    object StorageEmptyDialog : StorageDialogState()
    object StorageCreateDialog : StorageDialogState()
    class StorageWarningDialog(val message: String) : StorageDialogState()
}

sealed class SideEffect {
    object Empty : SideEffect()

    class Message(
        val text: String,
        val labelAction: String? = null,
        val action: (() -> Unit)? = null,
    ) : SideEffect()
}

suspend fun IntentScope<*, SideEffect>.message(
    text: String,
    labelAction: String? = null,
    action: (() -> Unit)? = null,
) {
    side(
        effect = SideEffect.Message(
            text = text,
            labelAction = labelAction,
            action = action
        )
    )
}