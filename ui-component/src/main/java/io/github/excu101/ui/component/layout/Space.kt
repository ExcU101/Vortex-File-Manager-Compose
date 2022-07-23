package io.github.excu101.ui.component.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ColumnSpacer(
    modifier: Modifier = Modifier,
    size: Dp
) {
    Spacer(modifier = modifier.height(size))
}

@Composable
fun RowSpacer(
    modifier: Modifier = Modifier,
    size: Dp
) {
    Spacer(modifier = modifier.width(size))
}
