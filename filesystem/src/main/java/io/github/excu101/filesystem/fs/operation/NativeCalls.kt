package io.github.excu101.filesystem.fs.operation

object NativeCalls {

    init {
        System.loadLibrary("unix-calls")
    }

    external fun close(descriptor: Int)

}