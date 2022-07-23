package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction

object ActionManager {

    private val _groups: MutableSet<GroupAction> = mutableSetOf()
    val groups: Set<GroupAction>
        get() = _groups

    fun registerGroup(group: GroupAction) {
        _groups.add(group)
    }

    fun registerAction(action: Action, groupName: String): Boolean {
        _groups.takeWhile {
            if (it.name == groupName) {
                return it.actions.add(action)
            } else {
                return false
            }
        }
        return false
    }

}