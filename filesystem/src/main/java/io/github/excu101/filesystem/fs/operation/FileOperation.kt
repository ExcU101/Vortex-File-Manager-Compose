package io.github.excu101.filesystem.fs.operation

abstract class FileOperation {
    abstract val id: Int
    abstract suspend fun perform()
}