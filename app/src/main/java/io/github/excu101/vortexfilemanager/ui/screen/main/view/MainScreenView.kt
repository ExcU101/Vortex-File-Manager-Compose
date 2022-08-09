package io.github.excu101.vortexfilemanager.ui.screen.main.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import io.github.excu101.ui.component.bar.TextFieldBar
import io.github.excu101.ui.component.button.IconButtonContainer
import io.github.excu101.ui.component.layout.RowSpacer
import io.github.excu101.vortexfilemanager.ui.MainScreenController
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.backgroundColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarActionIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarNavigationIconTintColorKey
import io.github.excu101.vortexfilemanager.ui.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortexfilemanager.ui.view.action.MenuAction

@Composable
fun MainScaffold(
    controller: MainScreenController,
    modifier: Modifier = Modifier,
    bar: @Composable () -> Unit = {
        val isHide by remember {
            controller.bar.isHide
        }

        var request by remember {
            controller.bar.request
        }
        val actions by remember {
            controller.bar.actions
        }

        val offset by animateDpAsState(
            if (!isHide) -WindowInsets.navigationBars.asPaddingValues()
                .calculateBottomPadding() else 64.dp + WindowInsets.navigationBars.asPaddingValues()
                .calculateBottomPadding()
        )

        TextFieldBar(
            color = Theme[mainBarSurfaceColorKey],
            modifier = Modifier
                .graphicsLayer { translationY = offset.toPx() },
            value = request,
            shape = RoundedCornerShape(0),
            onValueChange = { newRequest ->
                request = newRequest
            },
            navigationIcon = {
                IconButtonContainer(
                    vector = MenuAction.icon,
                    contentDescription = MenuAction.title,
                    onClick = { controller.notifyActionListeners(MenuAction) },
                    tint = Theme[mainBarNavigationIconTintColorKey]
                )
                Spacer(modifier = Modifier.width(32.dp))
            },
            actions = {
                Spacer(modifier = Modifier.weight(1F, true))
                actions.forEach { action ->
                    RowSpacer(size = 24.dp)
                    IconButtonContainer(
                        vector = action.icon,
                        contentDescription = action.title,
                        onClick = { controller.notifyActionListeners(action = action) },
                        tint = Theme[mainBarActionIconTintColorKey]
                    )
                }
            }
        )
    },
    snackbar: @Composable () -> Unit = { SnackbarHost(hostState = controller.snackbar) },
    content: @Composable (PaddingValues) -> Unit,
) {
    Surface(
        modifier = modifier,
        color = remember(Theme.getColor(backgroundColorKey)) {
            Theme.getColor(backgroundColorKey)
        }
    ) {
        MainScaffoldLayout(
            bar = bar,
            snackbar = snackbar,
            content = content,
        )
    }
}

@Composable
private fun MainScaffoldLayout(
    bar: @Composable () -> Unit,
    snackbar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {

            val bottomBarPlaces =
                subcompose(slotId = "bar", content = bar).fastMap { it.measure(looseConstraints) }

            val bottomBarHeight = bottomBarPlaces.fastMaxBy { it.height }?.height ?: 0

            val bottomBarWidth = bottomBarPlaces.fastMaxBy { it.width }?.width ?: 0

            val snackbarPlaces = subcompose(slotId = "snackbar", content = snackbar).fastMap {
                it.measure(looseConstraints)
            }
            val snackbarHeight = snackbarPlaces.fastMaxBy { it.height }?.height ?: 0

            val snackbarOffsetFromBottom = if (snackbarHeight != 0) {
                snackbarHeight + (bottomBarHeight)
            } else {
                0
            }

            val bodyContentPlaces = subcompose(slotId = "content") {
                content(PaddingValues(bottom = bottomBarHeight.toDp()))
            }.fastMap { it.measure(looseConstraints.copy(maxHeight = layoutHeight)) }

            bodyContentPlaces.fastForEach {
                it.place(x = 0, y = 0)
            }

            snackbarPlaces.fastForEach {
                it.place(x = 0, y = layoutHeight - snackbarOffsetFromBottom)
            }

            bottomBarPlaces.fastForEach {
                it.place(x = 0, y = layoutHeight - bottomBarHeight)
            }
        }
    }
}