package io.github.excu101.ui.component.bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.ui.component.button.*

@Composable
fun TextFieldBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    navigationIcon: ImageVector,
    onNavigationClick: () -> Unit,
    actions: Collection<ImageVector>,
    onActionClick: (Int) -> Unit,
    shape: Shape = RoundedCornerShape(16.dp),
    textStyle: TextStyle = TextStyle(
        fontSize = 18.sp
    )
) {
    TextFieldBar(
        modifier = modifier,
        textStyle = textStyle,
        shape = shape,
        value = value,
        onValueChange = onValueChange,
        navigationIcon = {
            IconButtonContainer(
                vector = navigationIcon,
                onClick = onNavigationClick
            )
        },
        actions = {
            actions.forEachIndexed { index, vector ->
                Spacer(modifier = Modifier.width(24.dp))
                IconButtonContainer(
                    vector = vector,
                    onClick = { onActionClick(index) }
                )
            }
        },
    )
}

@Composable
fun TextFieldBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    color: Color = MaterialTheme.colors.surface,
    navigationIcon: (@Composable RowScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    textStyle: TextStyle = TextStyle(
        fontSize = 18.sp
    )
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    var lastTextValue by remember(value) { mutableStateOf(value) }

    SurfaceBar(
        modifier = modifier
            .heightIn(min = 48.dp),
        color = color,
        elevation = 8.dp,
        shape = shape,
    ) {
        if (navigationIcon != null) {
            navigationIcon()
        }
        BasicTextField(
            value = textFieldValue,
            singleLine = true,
            onValueChange = {
                textFieldValueState = it

                val stringChangedSinceLastInvocation = lastTextValue != it.text
                lastTextValue = it.text

                if (stringChangedSinceLastInvocation) {
                    onValueChange(it.text)
                }
            },
            textStyle = textStyle,
        )
        if (actions != null) {

            actions()
        }
    }
}

@Preview
@Composable
private fun Preview() = MaterialTheme {
    var text by remember { mutableStateOf("") }
    TextFieldBar(
        modifier = Modifier.padding(16.dp),
        navigationIcon = {
            IconButtons.Outlined.Menu {

            }
        },
        actions = {
            IconButtons.Outlined.Sort {

            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButtons.Outlined.MoreVert {

            }
        },
        value = text,
        onValueChange = { text = it }
    )
}