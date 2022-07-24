package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.data.FileModelSet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StorageList(
    modifier: Modifier = Modifier,
    data: FileModelSet,
    selected: FileModelSet,
    state: LazyListState = rememberLazyListState(),
    onIconClick: (FileModel) -> Unit,
    onItemClick: (FileModel) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        state = state
    ) {
        items(items = data.models, key = { item -> item.path.hashCode() }) { model ->
            StorageItem(
                isSelected = selected.contains(model),
                model = model,
                modifier = Modifier.animateItemPlacement(),
                onItemClick = { onItemClick(model) },
                onIconClick = { onIconClick(model) }
            )
        }
    }

}