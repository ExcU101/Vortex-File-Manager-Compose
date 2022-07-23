package io.github.excu101.vortexfilemanager.provider.job

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.observer
import io.github.excu101.filesystem.unix.operation.UnixDeleteOperation
import io.github.excu101.vortexfilemanager.provider.ErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow

class DeleteJob(
    private val paths: Collection<Path>,
    private val progress: MutableStateFlow<Int>,
    private val errorHandler: ErrorHandler,
    private val onComplete: () -> Unit = {

    },
) {

    fun run() {
        FileProvider.runOperation(
            operation = UnixDeleteOperation(
                data = paths.toSet(),
                observer = observer(
                    onNext = {

                    },
                    onError = {
                        errorHandler.onError(it)
                    },
                    onComplete = onComplete
                )
            )
        )
    }

}