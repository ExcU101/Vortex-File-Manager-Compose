package io.github.excu101.filesystem.unix.path

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixFileSystem
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlin.math.min

@Parcelize
class UnixPath(
    private val fs: UnixFileSystem,
    private val path: ByteArray
) : Path, Parcelable {

    companion object : Parceler<UnixPath> {
        override fun create(parcel: Parcel): UnixPath {
            return parcel.readString()?.let { FileProvider.parsePath(it) } as UnixPath
        }

        override fun UnixPath.write(parcel: Parcel, flags: Int) {
            parcel.writeString(path.decodeToString())
        }
    }

    private val separatorPoints: List<Int>

    init {
        var index = 0
        val list = mutableListOf<Int>()
        while (index < path.size) {
            if (path[index] == fs.separator) {
                list.add(index)
                index++
            } else {
                while (index < path.size && path[index] != fs.separator) {
                    index++
                }
            }
        }
        separatorPoints = list
    }

    override val bytes: ByteArray
        get() = path

    override val fileSystem: FileSystem
        get() = fs

    override val nameCount: Int
        get() = separatorPoints.size - 1

    override val root: Path
        get() = UnixPath(fs, path = byteArrayOf(fs.separator))

    override val length: Int
        get() = path.size

    override val parent: Path?
        get() = when (nameCount) {
            0 -> null
            1 -> root
            else -> root.resolve(
                other = sub(index = separatorPoints[nameCount])
            )
        }

    override val isEmpty: Boolean
        get() = path.isEmpty()

    override val isHidden: Boolean
        get() = fs.provider.isHidden(source = this)

    override val isAbsolute: Boolean
        get() = !isEmpty && path[0] == fs.separator

    override fun startsWith(other: Path): Boolean {
        if (other.length > length) return false

        val otherNameCount = other.nameCount
        if (otherNameCount == 0 && isAbsolute) return !other.isEmpty

        if (otherNameCount > nameCount) return false

        if (otherNameCount == nameCount && length != other.length) return false

        for (index in 0..other.length) {
            if (bytes[index] != other.bytes[index]) {
                return false
            }
        }

        return true
    }

    override fun endsWith(other: Path): Boolean {
        return true
    }

    override fun normalize(): Path {
        val normalizedSegments = mutableListOf<Byte>()
        for (byte in bytes) {
            if (byte == '.'.code.toByte()) {
//
            } else if (byte == "..".toByte()) {
                if (normalizedSegments.isEmpty()) {
                    if (!isAbsolute) {
                        normalizedSegments += byte
                    }
                } else {
                    if (normalizedSegments.last() == "..".toByte()) {
                        normalizedSegments += byte
                    } else {
                        normalizedSegments.removeLast()
                    }
                }
            } else {
                normalizedSegments += byte
            }
        }
        if (!isAbsolute && normalizedSegments.isEmpty()) {
            return UnixPath(fs = fs, path = byteArrayOf(0))
        }

        return UnixPath(fs = fs, path = normalizedSegments.toByteArray())
    }

    private fun resolve(base: ByteArray, child: ByteArray): ByteArray {
        val baseLength = base.size
        val childLength = child.size
        val result: ByteArray
        if (baseLength == 1 && base[0] == fs.separator) {
            result = ByteArray(size = childLength + 1)
            result[0] = fs.separator
            System.arraycopy(child, 0, result, 1, childLength)
        } else {
            result = ByteArray(size = baseLength + 1 + childLength)
            System.arraycopy(base, 0, result, 0, baseLength)
            result[base.size] = fs.separator
            System.arraycopy(child, 0, result, baseLength + 1, childLength)
        }

        return result
    }

    override fun resolve(other: Path): Path {
        if (other.isEmpty) return this

        if (other.isAbsolute) return other

        if (isEmpty) return other

        return UnixPath(
            fs = fs,
            path = resolve(bytes, other.bytes)
        )
    }

    override fun relativize(other: Path): Path {
        if (other == this) return UnixPath(fs = fs, path = byteArrayOf(0))
        require(value = isAbsolute == other.isAbsolute) {
            "FUCK"
        }

        if (isEmpty) return other

        val size = length
        val otherSize = other.length
        val minimal = min(size, otherSize)
        var common = 0
        while (common < minimal && bytes[common] == other.bytes[common]) {
            ++common
        }
        val relatives = mutableListOf<Byte>()
        val dotCount = size - common

        if (dotCount > 0) {
            repeat(dotCount) { relatives += "..".toByte() }
        }
        if (common < otherSize) {
            relatives.addAll(other.bytes.copyOfRange(common, otherSize).toTypedArray())
        }

        return UnixPath(fs = fs, path = relatives.toByteArray())
    }

    override fun getName(index: Int): Path {
        if (nameCount == 0 || index < 0) return this
        val begin = separatorPoints[index] + 1
        val end = separatorPoints.getOrNull(index = index + 1) ?: length

        return sub(from = begin, to = end)
    }

    private fun sub(index: Int): Path = sub(0, index)

    override fun sub(from: Int, to: Int): Path = UnixPath(
        fs = fs,
        path = path.copyOfRange(from, to)
    )

    override fun toString(): String {
        return path.decodeToString()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is UnixPath) return false

        return compareTo(other) == 0
    }


    override fun compareTo(other: Path): Int {
        return (path.size - other.bytes.size)
    }

    override fun hashCode(): Int {
        return path.contentHashCode()
    }
}