package io.github.excu101.ui.component.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun Subtitle(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = SubtitleTextStyle,
    maxLines: Int = 1,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = maxLines
    )
}

val SubtitleTextStyle = TextStyle(
    fontSize = 14.sp
)