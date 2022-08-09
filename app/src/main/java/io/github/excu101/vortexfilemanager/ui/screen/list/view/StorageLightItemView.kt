package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.ui.component.layout.RowSpacer
import io.github.excu101.ui.component.layout.SurfaceRow
import io.github.excu101.ui.component.text.Subtitle
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.fileItemSurfaceColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileItemSurfaceSelectedColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileItemTitleSelectedTextColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.fileItemTitleTextColorKey

@Composable
fun StorageLightItemView(
    modifier: Modifier = Modifier,
    model: FileModel,
    isSelected: Boolean = false,
    onItemClick: () -> Unit = { },
    onSelect: () -> Unit = {}
) {
    val transition = updateTransition(targetState = isSelected, label = "selection")

    val surfaceColor by transition.animateColor(
        label = "surface",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemSurfaceSelectedColorKey
            else
                fileItemSurfaceColorKey
        }
    }

    val titleColor by transition.animateColor(
        label = "title",
        transitionSpec = { tween(300) }
    ) {
        Theme {
            if (it)
                fileItemTitleSelectedTextColorKey
            else
                fileItemTitleTextColorKey
        }
    }

    SurfaceRow(
        color = surfaceColor,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .clickable(onClick = onItemClick)
            .padding(16.dp),
        alignment = Alignment.CenterVertically
    ) {
        Subtitle(text = if (model.isDirectory) "D" else "F")
        RowSpacer(size = 16.dp)
        Text(
            modifier = Modifier.clickable(onClick = onSelect),
            text = model.name,
            style = TextStyle(
                color = titleColor,
                fontSize = 16.sp
            ),
        )
    }
}