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

@Composable
fun FileCreateDialog(
    shows: Boolean,
    onDismissRequest: () -> Unit,
    onCancelRequest: () -> Unit,
    onConfirmRequest: (isDirectory: Boolean, path: Path) -> Unit
) {
    if (shows) {
        var name by remember {
            mutableStateOf("")
        }
        var isDirectory by remember {
            mutableStateOf(false)
        }

        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        shape = RoundedCornerShape(16.dp),
                        label = {
                            Text(text = "Name", textAlign = TextAlign.Start)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(color = 0xFF3062FF),
                            focusedLabelColor = Color(color = 0xFF3062FF),
                            cursorColor = Color(color = 0xFF3062FF)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 16.dp),
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
                        Text(text = "Is directory", modifier = Modifier.clickable(
                            onClick = {
                                isDirectory = !isDirectory
                            },
                        ))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 8.dp)
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
                        Spacer(modifier = Modifier.width(8.dp))
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
}