package io.github.excu101.vortexfilemanager.ui.screen.info

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.excu101.filesystem.fs.path.Path

@Composable
fun FileInfoScreen(
    path: Path
) {
    Text(text = path.toString(), Modifier.fillMaxSize())
}