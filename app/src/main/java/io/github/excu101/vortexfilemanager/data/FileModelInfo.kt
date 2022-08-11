package io.github.excu101.vortexfilemanager.data

import androidx.compose.runtime.Stable
import io.github.excu101.vortexfilemanager.data.FileModelInfo.Companion.parseContent

@Stable
data class FileModelInfo(
    val name: String,
    val type: String,
    val size: String,
    val content: String,
) {

    companion object {
        internal fun FileModel.parseContent(): String {
            if (isDirectory) {
                val props = properties

                if (props.count == 1) {
                    return "Single item"
                }

                if (props.count > 1) {
                    return "${props.files.count()} files, ${props.dirs.count()} folders"
                }

                return "No item"
            } else {
                return ""
            }
        }
    }

}

fun FileModel.toInfo(): FileModelInfo {
    return FileModelInfo(
        name = name,
        type = if (isDirectory) "Folder" else "File",
        size = size.toString(),
        content = parseContent()
    )
}