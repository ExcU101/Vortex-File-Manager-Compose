package io.github.excu101.filesystem.fs.attr.time

import java.text.SimpleDateFormat
import java.util.*

class FileTime(
    private val instant: Instant
) {

    fun isZero(): Boolean = instant == null

    fun toNanos() = instant.nanos

    fun toSeconds() = instant.seconds

    override fun toString(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date(toSeconds()))
    }

}