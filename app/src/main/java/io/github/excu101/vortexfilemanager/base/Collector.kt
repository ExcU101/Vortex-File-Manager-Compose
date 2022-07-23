package io.github.excu101.vortexfilemanager.base

interface Collector<S> {

    val states: List<S>

    fun emit(state: S)

}