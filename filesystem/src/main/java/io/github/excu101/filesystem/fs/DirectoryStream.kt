package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.path.Path
import java.io.Closeable

interface DirectoryStream<P : Path> : Iterable<Path>, Closeable {

    override fun close()

    interface Filter<T> {
        fun accept(value: T): Boolean

        companion object {

            fun <T> acceptAll() = object : Filter<T> {
                override fun accept(value: T): Boolean {
                    return true
                }
            }


        }
    }

}