package io.github.excu101.ui.component.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.excu101.ui.component.layout.ProgressBarColors.Companion.colors

interface ProgressBarColors {

    companion object {
        @Composable
        fun colors(
            backgroundColor: Color = MaterialTheme.colors.background,
            barColor: Color = MaterialTheme.colors.primary,
            messageColor: Color = MaterialTheme.colors.onSurface,
            buttonTextColor: Color = MaterialTheme.colors.secondary,
        ): ProgressBarColors {
            return ProgressBarColorsImpl(
                backgroundColor,
                barColor,
                messageColor,
                buttonTextColor
            )
        }
    }

    @Composable
    fun backgroundColor(): State<Color>

    @Composable
    fun barColor(): State<Color>

    @Composable
    fun messageColor(): State<Color>

    @Composable
    fun buttonTextColor(): State<Color>

}

internal class ProgressBarColorsImpl(
    private val backgroundColor: Color,
    private val barColor: Color,
    private val messageColor: Color,
    private val buttonTextColor: Color,
) : ProgressBarColors {

    @Composable
    override fun backgroundColor(): State<Color> = rememberUpdatedState(newValue = backgroundColor)

    @Composable
    override fun barColor(): State<Color> = rememberUpdatedState(newValue = barColor)

    @Composable
    override fun messageColor(): State<Color> = rememberUpdatedState(newValue = messageColor)

    @Composable
    override fun buttonTextColor(): State<Color> = rememberUpdatedState(newValue = buttonTextColor)
}

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
    colors: ProgressBarColors = colors(),
    action: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = colors.backgroundColor().value
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                color = colors.barColor().value
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!message.isNullOrEmpty()) {
                Text(
                    text = message,
                    color = colors.messageColor().value,
                    style = textStyle
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            TextButton(
                onClick = action,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.buttonTextColor().value
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