package io.github.excu101.ui.component.layout

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> AnimatedLazyColumn(
    modifier: Modifier = Modifier,
    paddings: PaddingValues = PaddingValues(),
    state: LazyListState = rememberLazyListState(),
    data: List<AnimatedItem<T>>,
    key: ((AnimatedItem<T>) -> Any)? = null,
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
    itemContent: @Composable AnimatedVisibilityScope.(value: T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = paddings,
        state = state,
        content = {
            items(
                items = data,
                key = key
            ) { item ->
                AnimatedVisibility(
                    visibleState = item.isVisible,
                    enter = enter,
                    exit = exit,
                ) {
                    itemContent.invoke(this, value = item.value)
                }
            }
        }
    )
}

class Models<T> {
    private val _items: MutableList<AnimatedItem<T>> = mutableListOf()
    val items: List<AnimatedItem<T>>
        get() = _items

    fun replace(data: Collection<AnimatedItem<T>>) {
        removeAll()
        _items.clear()
        _items.addAll(data)
    }

    fun removeAll() {
        _items.forEach {
            it.isVisible.targetState = false
        }
    }

    fun add(value: T) {
        _items.add(
            element = AnimatedItem(
                isVisible = MutableTransitionState(false).apply { targetState = true },
                value = value
            )
        )
    }

    fun remove(value: T) {
        val item = AnimatedItem(value = value)
        item.isVisible.targetState = false
    }
}

data class AnimatedItem<T>(
    val isVisible: MutableTransitionState<Boolean> = MutableTransitionState(initialState = true),
    val value: T
)