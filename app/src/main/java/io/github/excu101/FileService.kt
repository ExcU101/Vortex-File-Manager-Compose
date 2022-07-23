package io.github.excu101

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.github.excu101.vortexfilemanager.ui.screen.list.FileAidlInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FileService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        return object : FileAidlInterface.Stub() {
            override fun log(message: String): Unit = CoroutineScope(IO).run {
                launch { Log.v("Loggable", message) }
            }
        }
    }
}

fun createExplicitIntent(context: Context): Intent =
    Intent(context, FileService::class.java).apply {
        action = "VORTEX_FILE_SERVICE"
    }