package io.github.excu101.vortexfilemanager.data.intent

import io.github.excu101.pluginsystem.model.Action

fun interface ActionListener {

    fun onCall(action: Action)

}