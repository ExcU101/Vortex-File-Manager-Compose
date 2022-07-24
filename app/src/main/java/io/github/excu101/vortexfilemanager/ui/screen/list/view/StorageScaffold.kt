package io.github.excu101.vortexfilemanager.ui.screen.list.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import io.github.excu101.vortexfilemanager.data.intent.Contracts

@Composable
fun StorageScaffold(
    trail: @Composable () -> Unit,
    additional: @Composable () -> Unit,
    dialog: @Composable () -> Unit,
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

            val dialogPlaces = subcompose("dialog", dialog).fastMap {
                it.measure(looseConstraints)
            }

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

            dialogPlaces.forEach {
                it.place(x = 0, y = 0)
            }
        }
    }
}

@Composable
fun StorageScaffold(
    targetState: Contracts.State.StorageScreenState,
    trail: @Composable () -> Unit,
    additional: @Composable () -> Unit,
    dialog: @Composable () -> Unit,
    content: @Composable (Contracts.State.StorageScreenState) -> Unit
) {
    StorageScaffold(
        trail = trail,
        additional = additional,
        dialog = dialog,
        content = {
            Box(
                modifier = Modifier.padding(it),
                content = { content(targetState) }
            )
        }
    )
}