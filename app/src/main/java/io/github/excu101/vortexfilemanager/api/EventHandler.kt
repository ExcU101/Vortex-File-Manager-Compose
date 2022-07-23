package io.github.excu101.vortexfilemanager.api

interface EventHandler<Event> {

    fun handleEvent(event: Event)

}