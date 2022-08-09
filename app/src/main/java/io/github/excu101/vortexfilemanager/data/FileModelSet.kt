package io.github.excu101.vortexfilemanager.data

import io.github.excu101.filesystem.fs.path.Path

class FileModelSet : AbstractMutableSet<FileModel>() {

    private val keyExtractor = FileModel::path

    operator fun get(key: Path) = state[key]

    val state: MutableMap<Path, FileModel> = mutableMapOf()

    override fun add(element: FileModel): Boolean {
        return state.put(keyExtractor(element), element) == null
    }

    override fun remove(element: FileModel): Boolean {
        return state.remove(keyExtractor(element)) != null
    }

    override fun clear() {
        state.clear()
    }

    override fun contains(element: FileModel): Boolean = state.containsKey(keyExtractor(element))

    override fun isEmpty(): Boolean = state.isEmpty()

    override fun iterator(): MutableIterator<FileModel> = state.values.iterator()

    override val size: Int
        get() = state.size

}