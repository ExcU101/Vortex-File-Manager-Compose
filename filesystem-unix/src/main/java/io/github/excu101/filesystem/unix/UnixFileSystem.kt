package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.FileSystemProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.path.UnixPath

class UnixFileSystem(
    override val provider: FileSystemProvider
) : FileSystem(provider = provider) {

    override val separator: Byte
        get() = '/'.code.toByte()

    private val defaultDirectory = UnixPath(
        fs = this,
        path = if (!System.getenv("user.dir").isNullOrEmpty()) {
            System.getenv("user.dir")!!.toByteArray()
        } else {
            byteArrayOf(separator)
        }
    )

    init {
        if (!defaultDirectory.isAbsolute) error("FUCK U")
    }

    override val scheme: String
        get() = "file"

    override fun isOpen(): Boolean = true

    override fun isReadOnly(): Boolean = false

    override fun getPath(first: String, vararg other: String): Path {
        return UnixPath(
            fs = this,
            path = StringBuilder(first).apply {
                other.forEach {
                    append(separator.toInt().toChar().toString())
                    append(it)
                }
            }.toString().encodeToByteArray()
        )
    }

}