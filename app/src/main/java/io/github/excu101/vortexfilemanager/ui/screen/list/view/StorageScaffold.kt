package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import io.github.excu101.vortexfilemanager.ui.screen.list.view.StorageScaffoldContentType.CONTENT
import io.github.excu101.vortexfilemanager.ui.screen.list.view.StorageScaffoldContentType.TRAIL

@Composable
private fun StorageScaffoldImpl(
    trail: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val trailPlaces = subcompose(slotId = TRAIL, trail).fastMap {
                it.measure(looseConstraints)
            }

            val trailHeight = trailPlaces.fastMaxBy { it.height }?.height ?: 0

            val contentPlaces = subcompose(slotId = CONTENT) {
                content(PaddingValues(top = trailHeight.toDp()))
            }.fastMap { it.measure(looseConstraints.copy(maxHeight = layoutHeight)) }

            contentPlaces.forEach {
                it.place(x = 0, y = 0)
            }

            trailPlaces.forEach {
                it.place(x = 0, y = 0)
            }

        }
    }
}

enum class StorageScaffoldContentType {
    TRAIL,
    CONTENT
}

@Composable
fun StorageScaffold(
    trail: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    StorageScaffoldImpl(
        trail = trail,
        content = content
    )
}