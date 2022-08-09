package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun FileListWarningDialog(
    onDismissRequest: () -> Unit,
    message: String = "",
    onConfirmRequest: () -> Unit = {},
    onCancelRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 128.dp)
                        .background(color = Color(0xFF3062FF)),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
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
                        onClick = onConfirmRequest,
                        modifier = Modifier,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF1744)
                        )
                    ) {
                        Text(text = "Confirm")
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}