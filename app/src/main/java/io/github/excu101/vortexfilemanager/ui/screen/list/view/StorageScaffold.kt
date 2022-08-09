package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy

@Composable
private fun StorageScaffoldImpl(
    trail: @Composable () -> Unit,
    additional: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val trailPlaces = subcompose("trailRow", trail).fastMap {
                it.measure(looseConstraints)
            }

            val trailHeight = trailPlaces.fastMaxBy { it.height }?.height ?: 0

            val additionalPlaces = subcompose("additionalMenu", additional).fastMap {
                it.measure(looseConstraints)
            }

            val additionalHeight = additionalPlaces.fastMaxBy { it.height }?.height ?: 0

            val contentPlaces = subcompose("content") {
//                bottom = additionalHeight.toDp(),
                content(PaddingValues(top = trailHeight.toDp()))
            }.fastMap { it.measure(looseConstraints.copy(maxHeight = layoutHeight)) }

            contentPlaces.forEach {
                it.place(x = 0, y = 0)
            }

            trailPlaces.forEach {
                it.place(x = 0, y = 0)
            }

            additionalPlaces.forEach {
                it.place(x = 0, y = layoutHeight - additionalHeight)
            }

        }
    }
}

@Composable
fun StorageScaffold(
    trail: @Composable () -> Unit,
    additional: @Composable () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    StorageScaffoldImpl(
        trail = trail,
        additional = additional,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                content = content
            )
        }
    )
}