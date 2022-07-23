package io.github.excu101.ui.component.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 1,
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = TitleTextStyle
    )
}

val TitleTextStyle = TextStyle(
    fontSize = 18.sp
)