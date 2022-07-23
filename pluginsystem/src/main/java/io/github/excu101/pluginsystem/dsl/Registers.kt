package io.github.excu101.pluginsystem.dsl

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.provider.ActionManager
import io.github.excu101.pluginsystem.provider.OperationManager
import kotlin.reflect.KClass

@DslMarker
annotation class RegistersMarker

@RegistersMarker
class Registers {

    data class RegistersGroup(
        var name: String,
        var icon: ImageVector? = null
    ) {
        internal val _actions: MutableSet<Action> = mutableSetOf()
        val actions: Set<Action>
            get() = _actions

        data class RegistersAction(
            var title: String = "",
            var icon: ImageVector = Icons.Outlined.Info,
            var operation: KClass<out FileOperation>? = null
        )

        fun register(action: Action, operation: KClass<out FileOperation>) {
            register(action.title, operation)
            register(action)
        }

        fun register(title: String, operation: KClass<out FileOperation>) {
            OperationManager.register(title, operation)
        }

        fun register(action: Action) {
            _actions.add(action)
        }

        inline fun Plugin.registerAction(block: RegistersAction.() -> Unit) {
            val actionBlock = RegistersAction().apply(block)

            register(
                Action(
                    title = actionBlock.title,
                    icon = actionBlock.icon,
                )
            )
            actionBlock.operation?.let {
                register(title = actionBlock.title, operation = it)
            }
        }
    }

    inline fun Plugin.registerGroup(block: RegistersGroup.() -> Unit) {
        val groupBlock = RegistersGroup(name = "").apply(block)

        ActionManager.registerGroup(
            GroupAction(
                name = groupBlock.name,
                actions = groupBlock.actions.toMutableSet()
            )
        )
    }
}

inline fun Plugin.registers(block: Registers.() -> Unit) {
    block(Registers())
}