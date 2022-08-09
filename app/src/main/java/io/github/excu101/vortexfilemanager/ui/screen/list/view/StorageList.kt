package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.excu101.ui.component.layout.AnimatedItem
import io.github.excu101.ui.component.layout.AnimatedLazyColumn
import io.github.excu101.ui.component.model.SelectableItem
import io.github.excu101.ui.component.utils.recomposeHighlighter
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.data.SelectableValue

@Composable
fun StorageList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    paddings: PaddingValues = PaddingValues(0.dp),
    data: List<FileModel>,
    selected: Collection<FileModel>,
    isLightModeEnabled: Boolean,
    onSelect: (FileModel) -> Unit,
    onItemClick: (FileModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier.recomposeHighlighter(),
        contentPadding = paddings,
        state = state,
    ) {
        items(
            items = data,
            key = { it.id },
            contentType = { it }
        ) { model ->

            if (isLightModeEnabled) {
                StorageLightItemView(
                    model = model,
                    isSelected = selected.contains(model),
                    onItemClick = { onItemClick(model) },
                    onSelect = { onSelect(model) }
                )
            } else {
                StorageItem(
                    model = model,
                    isSelected = selected.contains(model),
                    onItemClick = { onItemClick(model) },
                    onSelect = { onSelect(model) }
                )
            }
        }
    }
}

object StorageListDefaults {
    val paddings: PaddingValues
        @Composable
        get() = WindowInsets.navigationBars.asPaddingValues()
}