package io.github.excu101.pluginsystem.model

interface Plugin {

    val attributes: Plugin.Attributes

    fun activate()

    interface Attributes {
        val name: String
        val version: String
        val packageName: String
    }

    enum class State {
        ACTIVATED,
        DISABLED
    }
}