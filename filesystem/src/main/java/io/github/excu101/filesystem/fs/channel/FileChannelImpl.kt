package io.github.excu101.filesystem.fs.channel

import android.os.ParcelFileDescriptor
import io.github.excu101.filesystem.fs.buffer.ByteBuffer
import io.github.excu101.filesystem.fs.operation.NativeCalls
import java.io.FileDescriptor

class FileChannelImpl private constructor(
    val descriptor: FileDescriptor,
    val path: String,
    val readable: Boolean,
    val writable: Boolean,
    val append: Boolean = false
) : FileChannel() {

    @Volatile
    private var open = true

    private var locker = Any()

    companion object {
        fun open(
            descriptor: FileDescriptor,
            path: String,
            readable: Boolean,
            writable: Boolean,
            append: Boolean = false
        ): FileChannel {
            return FileChannelImpl(
                descriptor = descriptor,
                path = path,
                readable = readable,
                writable = writable,
                append = append
            )
        }
    }

    override fun read(dest: ByteBuffer): Int {
        require(!readable)
        synchronized(lock = locker) {
            var n = 0
            val ti = -1
            try {
                if (!isOpen) return 0
                do {
                    n
                } while ((n == -3) && isOpen)
                return if (n == -2) 0 else n
            } finally {

            }
        }
    }

    override fun write(src: ByteBuffer): Int {
        return 0
    }

    override val size: Long
        get() = 0L

    override val position: Int
        get() = 0

    override fun newPosition(position: Int): FileChannel {
        return this
    }

    override fun truncate(size: Long): FileChannel {
        return this
    }

    override val isOpen: Boolean
        get() = open

    override fun implCloseChannel() {
        NativeCalls.close(ParcelFileDescriptor.dup(descriptor).fd)
    }

    override fun close() {
        implCloseChannel()
    }

}