package io.github.excu101.ui.component.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

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
    message: String? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    textButton: String,
    backgroundColor: Color = Color.White,
    progressColor: Color = MaterialTheme.colors.primary,
    messageColor: Color = Color.Black,
    buttonTextColor: Color = Color.Black,
    action: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = backgroundColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                color = progressColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!message.isNullOrEmpty()) {
                Text(
                    text = message,
                    color = messageColor,
                    style = textStyle
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            TextButton(
                onClick = action,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = buttonTextColor
                )
            ) {
                Text(text = textButton)
            }
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