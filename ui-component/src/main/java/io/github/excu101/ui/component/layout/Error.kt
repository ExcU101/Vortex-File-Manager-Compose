package io.github.excu101.ui.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String,
    style: TextStyle = ErrorTextStyle,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = { Text(text = message, style = style) }
    )
}

val ErrorTextStyle = TextStyle(
    fontSize = 20.sp
)