package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.data.FileModelSet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListView(
    modifier: Modifier = Modifier,
    data: FileModelSet,
    selected: FileModelSet,
    state: LazyListState = rememberLazyListState(),
    onIconClick: (FileModel) -> Unit,
    onItemClick: (FileModel) -> Unit,
) {
    val _data by derivedStateOf { data }

    val _selected by derivedStateOf { selected }

    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        state = state
    ) {
        _data.forEachIndexed { index, model ->
            item(key = model.path.hashCode()) {
                FileItemView(
                    index = index,
                    isSelected = _selected.contains(model),
                    model = model,
                    modifier = Modifier.animateItemPlacement(),
                    onItemClick = { onItemClick(model) },
                    onIconClick = { onIconClick(model) }
                )
            }
        }
    }

}