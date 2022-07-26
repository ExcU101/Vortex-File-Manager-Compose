package io.github.excu101.pluginsystem.utils

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin

interface GroupScope {
    var title: String
    var icon: ImageVector?
    val items: Collection<Action>

    fun item(action: Action)
}

@PublishedApi
internal class GroupScopeImpl(override var title: String) : GroupScope {

    override var icon: ImageVector? = null

    private val _items = mutableListOf<Action>()

    override val items: Collection<Action>
        get() = _items

    override fun item(action: Action) {
        _items.add(action)
    }

    @PublishedApi
    internal fun toGroup(): GroupAction = GroupAction(
        name = title,
        icon = icon,
        actions = items
    )
}

inline fun group(title: String, block: GroupScope.() -> Unit): GroupAction {
    return GroupScopeImpl(title).apply(block).toGroup()
}

inline fun Plugin.group(block: GroupScope.() -> Unit): GroupAction {
    return group(title = attributes.name, block = block)
}

fun GroupScope.item(title: String, icon: ImageVector) {
    return item(Action(title = title, icon = icon))
}