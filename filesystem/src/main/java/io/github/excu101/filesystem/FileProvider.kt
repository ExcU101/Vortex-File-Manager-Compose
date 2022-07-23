package io.github.excu101.filesystem

import io.github.excu101.filesystem.fs.DirectoryStream
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

object FileProvider {

    private val systems = arrayListOf<FileSystem>()

    val systemCount: Int
        get() = systems.size

    fun newDirStream(path: Path): DirectoryStream<Path> {
        try {
            return systems.last().provider.newDirectorySteam(path)
        } catch (e: Exception) {
            throw e
        }
    }

    fun isDirectory(path: Path): Boolean {
        return systems.last().provider.readAttrs(path, BasicAttrs::class).isDirectory
    }

    fun <T : BasicAttrs> readAttrs(path: Path, type: KClass<T>): T {
        return systems.last().provider.readAttrs(path, type)
    }

    inline fun <reified T : BasicAttrs> readAttrs(path: Path): T = readAttrs(path, T::class)

    fun installFileSystem(system: FileSystem) {
        if (!systems.contains(system)) {
            systems.add(system)
        }
    }

    fun runOperation(operation: FileOperation): Job {
        return systems.last().provider.runOperation(operation = operation)
    }

    fun parsePath(input: String): Path {
        return systems.last().getPath(first = input)
    }

    fun parsePath(first: String, vararg other: String): Path {
        return systems.last().getPath(first = first, other = other)
    }

}