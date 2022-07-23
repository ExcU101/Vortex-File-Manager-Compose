package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.Observer
import io.github.excu101.filesystem.fs.attr.Option
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path

class UnixCreateFileOperation(
    private val path: Path,
    private val flags: Set<Option>,
    private val mode: Int,
    private val observer: Observer<Path>? = null
) : FileOperation() {

    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        try {
            path.fileSystem.provider.newFileChannel(path, flags, mode).close()
        } catch (exception: Exception) {
            observer?.onError(error = exception)
        }
        observer?.onComplete()
    }

}