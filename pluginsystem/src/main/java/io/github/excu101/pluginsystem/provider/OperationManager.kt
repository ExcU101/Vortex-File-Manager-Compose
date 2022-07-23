package io.github.excu101.pluginsystem.provider

import io.github.excu101.filesystem.fs.operation.FileOperation
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.primaryConstructor

object OperationManager {

    private val _operations: MutableMap<String, KClass<out FileOperation>> = mutableMapOf()
    val availableOperations: Collection<KClass<out FileOperation>>
        get() = _operations.values

    fun register(title: String, operation: KClass<out FileOperation>) {
        _operations[title] = operation
    }

    fun provide(title: String, vararg args: Any?): FileOperation? {
        return _operations[title]?.primaryConstructor?.call(args)
    }

    suspend fun suspendProvide(title: String, vararg args: Any?): FileOperation? {
        return _operations[title]?.primaryConstructor?.callSuspend(args)
    }

}