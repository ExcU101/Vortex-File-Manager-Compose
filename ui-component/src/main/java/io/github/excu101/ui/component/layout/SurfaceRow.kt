package io.github.excu101.ui.component.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SurfaceRow(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    color: Color = MaterialTheme.colors.surface,
    alignment: Alignment.Vertical = Alignment.Top,
    arrangement: Arrangement.Horizontal = Arrangement.Start,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        color = color
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            verticalAlignment = alignment,
            horizontalArrangement = arrangement,
            content = content
        )
    }
}