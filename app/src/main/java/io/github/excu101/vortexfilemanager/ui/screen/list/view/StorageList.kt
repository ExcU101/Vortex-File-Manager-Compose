package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.excu101.ui.component.utils.recomposeHighlighter
import io.github.excu101.vortexfilemanager.data.FileModel

@Composable
fun StorageList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    paddings: PaddingValues = PaddingValues(0.dp),
    data: List<FileModel>,
    selected: Collection<FileModel>,
    mode: StorageListViewMode,
    onSelect: (FileModel) -> Unit,
    onItemClick: (FileModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = paddings,
        state = state,
    ) {
        items(
            items = data,
            key = FileModel::id,
            contentType = { it }
        ) { model ->
            val selectListener = remember(data) { { onSelect(model) } }
            val itemListener = remember(data) { { onItemClick(model) } }

            when (mode) {
                StorageListViewMode.LIGHT -> {
                    StorageLightItemView(
                        model = model,
                        isSelected = selected.contains(model),
                        onItemClick = itemListener,
                        onSelect = selectListener
                    )
                }
                StorageListViewMode.NORMAL -> {
                    StorageItem(
                        model = model,
                        isSelected = selected.contains(model),
                        onItemClick = itemListener,
                        onSelect = selectListener
                    )
                }
            }
        }
    }
}

object StorageListDefaults {
    val paddings: PaddingValues
        @Composable
        get() = WindowInsets.navigationBars.asPaddingValues()
}