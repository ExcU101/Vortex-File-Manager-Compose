package io.github.excu101.vortexfilemanager.ui.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Banner(
    visible: Boolean,
    text: String,
    firstTitle: String,
    secondTitle: String,
    firstAction: () -> Unit,
    secondAction: () -> Unit,
    modifier: Modifier = Modifier,
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
) {
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = text, modifier = Modifier.padding(16.dp))
            Row(
                Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)
            ) {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = firstAction
                ) {
                    Text(text = firstTitle)
                }
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = secondAction
                ) {
                    Text(text = secondTitle)
                }
            }
        }
    }

}