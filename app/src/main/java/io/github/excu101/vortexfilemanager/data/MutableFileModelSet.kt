package io.github.excu101.vortexfilemanager.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.snapshots.StateObject
import androidx.compose.runtime.snapshots.StateRecord
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortexfilemanager.ui.util.stateOf

class MutableFileModelSet : AbstractMutableSet<FileModel>(), StateObject {

    private val keyExtractor = FileModel::path

    operator fun get(key: Path) = state[key]

    private val state: SnapshotStateMap<Path, FileModel> = mutableStateMapOf()

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

    override val firstStateRecord: StateRecord
        get() = state.firstStateRecord

    override fun prependStateRecord(value: StateRecord) {
        state.prependStateRecord(value)
    }

}