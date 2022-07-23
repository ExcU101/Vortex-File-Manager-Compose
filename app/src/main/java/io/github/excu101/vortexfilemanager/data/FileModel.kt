package io.github.excu101.vortexfilemanager.data

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.DirectoryProperties
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.filesystem.unix.attr.UnixAttributes
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission
import io.github.excu101.vortexfilemanager.util.MapSet
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class FileModel(
    val path: Path,
) : Parcelable {

    val attrs: UnixAttributes
        get() = FileProvider.readAttrs(path)

    val isHidden: Boolean
        get() = path.isHidden

    val isDirectory: Boolean
        get() = attrs.isDirectory

    val isFile: Boolean
        get() = attrs.isFile

    val isLink: Boolean
        get() = attrs.isLink

    val properties: DirectoryProperties
        get() = path.properties()

    val lastModifiedTime: String
        get() = attrs.lastModifiedTime.toString()

    val name: String
        get() = path.getName().toString()

    val mimeType: MimeType
        get() = MimeType.fromName(name = name)

    val size: Size
        get() = attrs.size

    val extension: String
        get() = mimeType.type

    val isReadable: Boolean
        get() = attrs.perms.containsAll(PosixPermission.readPerms)

    val isWritable: Boolean
        get() = attrs.perms.containsAll(PosixPermission.writePerms)

    val isExecutable: Boolean
        get() = attrs.perms.containsAll(PosixPermission.executePerms)

    companion object : Parceler<FileModel> {
        override fun create(parcel: Parcel): FileModel {
            return FileModel(path = (parcel.readString() ?: "").toPath())
        }

        override fun FileModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(path.toString())
        }
    }

}

fun FileModel.Companion.Saver() = Saver<FileModel, String>(
    save = {
        it.path.toString()
    },
    restore = {
        FileModel(it.toPath())
    }
)

class FileModelSet : MapSet<Path, FileModel>(FileModel::path) {

    override operator fun plus(element: FileModel) = fileModelSetOf(this)

    val models: List<FileModel> = mutableListOf<FileModel>().apply { addAll(this) }

    fun sort(comparator: Comparator<Path>): FileModelSet {
        return fileModelSetOf(map.toSortedMap(comparator).values)
    }

    companion object : Parceler<FileModelSet> {
        override fun FileModelSet.write(parcel: Parcel, flags: Int) {
            parcel.writeTypedList(toList())
            parcel.writeList(toList())
        }

        override fun create(parcel: Parcel): FileModelSet {
            val items = ArrayList<FileModel>()
            parcel.readList(
                items,
                FileModel::class.java.classLoader
            )

            return FileModelSet().apply {
                addAll(
                    items
                )
            }
        }
    }
}

fun fileModelSetOf(models: Iterable<FileModel>) = FileModelSet().apply {
    addAll(models)
}

fun fileModelSetOf(vararg model: FileModel) = FileModelSet().apply {
    addAll(model)
}