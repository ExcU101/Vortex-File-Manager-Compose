package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.Observer
import io.github.excu101.filesystem.fs.error.SystemCallException
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixCalls

class UnixCreateDirectoryOperation(
    private val path: Path,
    private val mode: Int,
    private val observer: Observer<Path>? = null
) : FileOperation() {

    override val id: Int
        get() = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        try {
            observer?.onNext(path)
            UnixCalls.mkdir(path.bytes, mode)
        } catch (exception: SystemCallException) {
            observer?.onError(exception)
            return
        }

        observer?.onComplete()
    }
}