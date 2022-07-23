package io.github.excu101.vortexfilemanager.ui.util

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressActionTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressBarTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.layoutProgressTitleTextColorKey

@Composable
fun ProgressBarView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ProgressBarView(
    modifier: Modifier = Modifier,
    text: String? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    textButton: String,
    action: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            color = Theme[layoutProgressBarTintColorKey]
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (!text.isNullOrEmpty()) {
            Text(text = text, color = Theme[layoutProgressTitleTextColorKey], style = textStyle)
            Spacer(modifier = Modifier.height(8.dp))
        }
        TextButton(
            onClick = action,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Theme[layoutProgressActionTintColorKey]
            )
        ) {
            Text(text = textButton)
        }
    }
}

@Composable
fun ProgressBarView(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(progress = progress)
    }
}