package io.github.excu101.vortexfilemanager.data

import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.attr.DirectoryProperties
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.filesystem.unix.attr.UnixAttributes
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileModel(
    val path: Path,
) : Parcelable {

    val id: Int = IdRegister.register(IdRegister.Type.PATH)

    val attrs: UnixAttributes
        get() = FileProvider.readAttrs(path)

    val parent: FileModel?
        get() = path.parent?.let(::FileModel)

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

        val SDModel = FileModel(Environment.getExternalStorageDirectory().asPath())

        override fun create(parcel: Parcel): FileModel {
            return FileModel(path = (parcel.readString() ?: "").toPath())
        }

        override fun FileModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(path.toString())
        }
    }

}