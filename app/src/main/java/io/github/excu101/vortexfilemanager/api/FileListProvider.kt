package io.github.excu101.vortexfilemanager.api

import io.github.excu101.filesystem.fs.path.Path

interface FileListProvider {

   suspend fun provide(path: Path): Collection<Path>

}