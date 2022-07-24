package io.github.excu101.vortexfilemanager.data

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.toPaths
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
class Trail(
    val segments: List<FileModel>,
    val selected: Int = segments.lastIndex,
) : Parcelable {

    companion object : Parceler<Trail> {
        override fun create(parcel: Parcel): Trail {
            val paths = parcel.createStringArrayList()?.toPaths()?.map(::FileModel) ?: emptyList()
            val selected = parcel.readInt()

            return Trail(
                segments = paths,
                selected = selected
            )
        }

        override fun Trail.write(parcel: Parcel, flags: Int) {
            parcel.writeStringList(segments.map { it.toString() })
            parcel.writeInt(selected)
        }
    }

    operator fun get(path: Path) = segments.contains(element = FileModel(path))

    operator fun get(paths: Collection<Path>) = segments.containsAll(paths.map(::FileModel))

    operator fun get(index: Int) = segments[index]

    operator fun set(current: Int = selected, index: Int) = navigateToSelected(index)

    val currentSelected: FileModel
        get() = if (selected < 0) FileModel(FileProvider.parsePath("")) else segments[selected]

    fun slice(paths: Collection<FileModel>, selected: Int = segments.lastIndex): Trail {
        var list = segments
        for (path in paths) {
            list = list.dropWhile { segment ->
                segment == path
            }
        }
        return Trail(segments = list, selected = selected)
    }

    fun navigateTo(path: Path) = navigateTo(newSegments = fromPathToModels(path = path))

    inline fun navigateTo(path: Path, block: (Path) -> Unit): Trail {
        val trail = navigateTo(newSegments = fromPathToModels(path = path))
        block(path)
        return trail
    }

    fun navigateTo(path: Path, selected: Int) =
        navigateTo(newSegments = fromPathToModels(path = path), selected = selected)

    fun navigateTo(
        newSegments: MutableList<FileModel>,
        selected: Int = newSegments.lastIndex,
        withPrefixChecking: Boolean = true,
    ): Trail {
        return if (withPrefixChecking) {
            var hasPrefix = true
            for (index in newSegments.indices) {
                if (hasPrefix && index < segments.size) {
                    if (segments[index] != newSegments[index]) hasPrefix = false
                }
            }
            if (hasPrefix) {
                for (index in newSegments.size until segments.size) {
                    newSegments.add(segments[index])
                }
            }
            Trail(segments = newSegments, selected = selected)
        } else {
            Trail(segments = newSegments, selected = selected)
        }

    }

    fun navigateToParent(): Trail {
        return Trail(segments.dropLast(n = 1))
    }

    fun navigateToSelected(index: Int): Trail {
        if (segments.size <= index) {
            throw IndexOutOfBoundsException()
        }

        return Trail(segments, index)
    }
}


fun fromPath(path: Path): MutableList<Path> {
    val list = mutableListOf<Path>()
    var cPath = path
    while (true) {
        list.add(cPath)
        cPath = cPath.parent ?: break
    }
    return list.asReversed()
}

fun fromPathToModels(path: Path): MutableList<FileModel> {
    return fromPath(path = path).map(::FileModel).toMutableList()
}