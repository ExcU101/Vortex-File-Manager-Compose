package io.github.excu101.vortexfilemanager.base

import android.util.Log

interface Logger {

    val tag: String

    val type: Int
        get() = Log.VERBOSE

    fun log(message: String)

}