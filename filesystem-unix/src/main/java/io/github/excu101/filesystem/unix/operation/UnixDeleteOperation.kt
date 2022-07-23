package io.github.excu101.filesystem.unix.operation

import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.Observer
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixCalls

class UnixDeleteOperation(
    private val data: Collection<Path>,
    private val observer: Observer<Path>? = null,
) : FileOperation() {

    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION)

    override suspend fun perform() {
        data.forEach { path ->
            try {
                observer?.onNext(path)
                UnixCalls.delete(path.bytes)
            } catch (error: Exception) {
                observer?.onError(error = error)
                return
            }
        }
        observer?.onComplete()
    }

}