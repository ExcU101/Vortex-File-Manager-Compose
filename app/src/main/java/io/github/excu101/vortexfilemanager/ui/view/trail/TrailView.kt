package io.github.excu101.vortexfilemanager.ui.view.trail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.*
import io.github.excu101.vortexfilemanager.ui.theme.themedColorAnimation

@Composable
fun TrailRow(
    modifier: Modifier = Modifier,
    segments: List<FileModel>,
    currentSelected: Int = segments.lastIndex,
    contentPaddings: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    ),
    scroller: LazyListState = rememberLazyListState(),
    scrollToSelected: Boolean = true,
    onTrailClick: (FileModel) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(
                min = 48.dp
            ),
        elevation = 8.dp,
        color = Theme[trailSurfaceColorKey]
    ) {
        LazyRow(
            modifier = Modifier,
            contentPadding = contentPaddings,
            state = scroller,
            content = {
                itemsIndexed(
                    items = segments,
                    key = { index, item -> item.path.hashCode() }
                ) { i, path ->
                    val isSelected = i == currentSelected
                    Row(
                        modifier = Modifier
                            .heightIn(48.dp)
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = rememberRipple(
                                    bounded = false,
                                    color = Theme {
                                        if (isSelected)
                                            trailItemTitleSelectedTextColorKey
                                        else
                                            trailItemTitleTextColorKey
                                    },
                                )
                            ) {
                                onTrailClick(path)
                            },
                            text = path.name,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = themedColorAnimation {
                                    if (isSelected)
                                        trailItemTitleSelectedTextColorKey
                                    else
                                        trailItemTitleTextColorKey
                                }
                            )
                        )
                        if (i != segments.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = null,
                                tint = themedColorAnimation {
                                    if (isSelected)
                                        trailItemArrowSelectedTintColorKey
                                    else
                                        trailItemArrowTintColorKey
                                }
                            )
                        }
                    }
                }
            }
        )
    }

    if (scrollToSelected) {
        LaunchedEffect(
            key1 = currentSelected,
            block = {
                if (currentSelected >= 0)
                    scroller.animateScrollToItem(index = currentSelected)
            }
        )
    }
}
