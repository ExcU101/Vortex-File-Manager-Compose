package io.github.excu101.filesystem.unix.error

class UnixException(
    val errno: Int,
    message: String?
) : Throwable(message)