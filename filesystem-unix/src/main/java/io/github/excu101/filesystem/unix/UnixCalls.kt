package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.unix.attr.UnixDirentStructure
import io.github.excu101.filesystem.unix.attr.UnixStatusStructure
import java.io.FileDescriptor

internal object UnixCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    external fun rename(source: ByteArray, dest: ByteArray)

    external fun stat(path: ByteArray): UnixStatusStructure

    external fun lstat(path: ByteArray): UnixStatusStructure

    external fun delete(path: ByteArray)

    external fun openDir(path: ByteArray): Long

    external fun open(path: ByteArray, flags: Int, mode: Int): FileDescriptor

    external fun mkdir(path: ByteArray, mode: Int)

    external fun readDir(pointer: Long): UnixDirentStructure?

    external fun close(descriptor: Int)

    external fun closeDir(pointer: Long)

}