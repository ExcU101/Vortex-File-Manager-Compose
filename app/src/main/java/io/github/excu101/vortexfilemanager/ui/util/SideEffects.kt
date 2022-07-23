package io.github.excu101.vortexfilemanager.ui.util

import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun Toast(
    text: String,
    duration: Int = LENGTH_SHORT
) = makeText(LocalContext.current, text, duration).show()