package io.github.excu101.vortexfilemanager.provider

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortexfilemanager.api.FileListProvider
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AndroidFileProvider @Inject constructor(
    private val context: Context,
) : FileListProvider {

    companion object {
        const val LAST_PATH_KEY = "LAST_PATH"
    }

    fun checkManagePerm(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        false
    }

    fun checkReadPerm(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
    }

    fun checkWritePerm(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE,
        ) == PERMISSION_GRANTED
    }

    fun requiresReadWritePerm(): Boolean {
        return !checkWritePerm() && !checkReadPerm()
    }

    override suspend fun provide(path: Path): Collection<Path> = withContext(IO) {
        try {
            FileProvider.newDirStream(path).toSet()
        } catch (e: Throwable) {
            throw e
        }
    }

}