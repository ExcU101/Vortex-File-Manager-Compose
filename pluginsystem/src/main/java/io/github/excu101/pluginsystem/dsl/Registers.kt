package io.github.excu101.pluginsystem.dsl

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.model.Screen
import io.github.excu101.pluginsystem.provider.Managers
import kotlin.reflect.KClass

@DslMarker
annotation class RegistersMarker

@RegistersMarker
class Registers(val plugin: Plugin) {

    inner class ScreenRegister(
        override var route: String = "",
        override var icon: ImageVector?,
        override var content: @Composable () -> Unit = {},
    ) : Screen

    fun Plugin.screen(route: String, icon: ImageVector? = null, block: @Composable () -> Unit) {
        Managers.Screen.register(plugin = this, ScreenRegister(route, icon, block))
    }

    inner class GroupRegister(
        var name: String,
        var icon: ImageVector? = null
    ) {
        private val _actions: MutableList<Action> = mutableListOf()
        val actions: Collection<Action>
            get() = _actions

        inner class RegistersAction(
            val plugin: Plugin,
            var title: String = "",
            var icon: ImageVector = Icons.Outlined.Info,
            var operation: KClass<out FileOperation>? = null
        ) {
            fun toAction(): Action {
                return Action(title, icon)
            }

            fun toPair(): Pair<Action, KClass<out FileOperation>?> {
                return toAction() to operation
            }
        }

        inline fun action(block: RegistersAction.() -> Unit) {
            val (action, operation) = RegistersAction(plugin).apply(block).toPair()
            register(
                Action(
                    title = action.title,
                    icon = action.icon,
                )
            )
            operation?.let {
                register(operation = it)
            }
        }

        fun register(action: Action, operation: KClass<out FileOperation>) {
            register(operation)
            register(action)
        }

        fun register(operation: KClass<out FileOperation>) {
            Managers.Operation.register(plugin, operation)
        }

        fun register(action: Action) {
            _actions.add(action)
        }

        fun unregister() {
            Managers.Operation.unregister(plugin)
        }
    }

    inline fun Plugin.registerGroup(block: GroupRegister.() -> Unit) {
        val groupBlock = GroupRegister(name = attributes.name).apply(block)

        Managers.Group.register(
            plugin = this,
            value = GroupAction(
                name = groupBlock.name,
                actions = groupBlock.actions
            )
        )
    }

    fun Plugin.unregisterGroup() {
        Managers.Group.unregister(plugin = this)
    }
}

inline fun Plugin.registers(block: Registers.() -> Unit) {
    Registers(plugin = this).apply(block)
}