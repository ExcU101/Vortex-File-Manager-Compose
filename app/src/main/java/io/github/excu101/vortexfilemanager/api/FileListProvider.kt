package io.github.excu101.vortexfilemanager.api

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortexfilemanager.data.FileModel

interface FileListProvider {

    suspend fun provide(path: Path): List<FileModel>

}