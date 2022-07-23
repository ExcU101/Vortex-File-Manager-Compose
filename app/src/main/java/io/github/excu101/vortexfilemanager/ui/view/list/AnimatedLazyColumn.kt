package io.github.excu101.vortexfilemanager.ui.view.list

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

interface AnimatedLazyListScope : LazyListScope {

    fun animatedItem(
        key: Any? = null,
        content: @Composable () -> Unit,
    )

}

@Composable
fun AnimatedLazyColumn(
    content: @Composable AnimatedLazyListScope.() -> Unit,
) {
    AnimatedLazyColumn {

    }
}