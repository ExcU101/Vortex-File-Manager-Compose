package io.github.excu101.vortexfilemanager.provider

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import android.os.Environment.isExternalStorageManager
import androidx.core.content.ContextCompat.checkSelfPermission
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortexfilemanager.api.FileListProvider
import io.github.excu101.vortexfilemanager.data.FileModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AndroidFileProvider @Inject constructor(
    private val context: Context,
) : FileListProvider {

    companion object {
        const val LAST_PATH_KEY = "LAST_PATH"
    }

    fun checkManagePerm(): Boolean = if (SDK_INT >= R) {
        isExternalStorageManager()
    } else {
        false
    }

    fun checkReadPerm(): Boolean {
        return checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
    }

    fun checkWritePerm(): Boolean {
        return checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE,
        ) == PERMISSION_GRANTED
    }

    fun requiresReadWritePerm(): Boolean {
        return !checkWritePerm() && !checkReadPerm()
    }

    override suspend fun provide(path: Path): List<FileModel> = withContext(IO) {
        FileProvider.newDirStream(path).use { stream -> stream.map { FileModel(it) } }
    }

}