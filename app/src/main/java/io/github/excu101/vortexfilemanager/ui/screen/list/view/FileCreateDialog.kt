package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.ui.component.layout.ColumnSpacer
import io.github.excu101.ui.component.layout.RowSpacer
import io.github.excu101.ui.component.text.TitleText

@Composable
fun FileCreateDialog(
    onDismissRequest: () -> Unit,
    onCancelRequest: () -> Unit,
    onConfirmRequest: (isDirectory: Boolean, path: Path) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isDirectory by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
            ) {
                TitleText(text = "Enter a name")
                ColumnSpacer(size = 16.dp)
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(color = 0xFF3062FF),
                        focusedLabelColor = Color(color = 0xFF3062FF),
                        cursorColor = Color(color = 0xFF3062FF)
                    )
                )
                ColumnSpacer(size = 8.dp)
                Row(
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDirectory,
                        onCheckedChange = {
                            isDirectory = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(color = 0xFF3062FF),
                        )
                    )
                    Text(
                        text = "Is directory", modifier = Modifier.clickable(
                            onClick = {
                                isDirectory = !isDirectory
                            },
                        )
                    )
                }
                ColumnSpacer(size = 8.dp)
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = onCancelRequest,
                        modifier = Modifier,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(color = 0xFF3062FF)
                        )
                    ) {
                        Text(text = "Cancel")
                    }
                    RowSpacer(size = 8.dp)
                    TextButton(
                        onClick = {
                            onConfirmRequest.invoke(
                                isDirectory,
                                FileProvider.parsePath(name)
                            )
                        },
                        modifier = Modifier,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF3062FF)
                        )
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}