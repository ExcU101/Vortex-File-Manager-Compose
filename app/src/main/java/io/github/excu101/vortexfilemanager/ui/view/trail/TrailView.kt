package io.github.excu101.vortexfilemanager.ui.view.trail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
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
import io.github.excu101.ui.component.layout.SurfaceLazyRow
import io.github.excu101.vortexfilemanager.data.FileModel
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.*
import io.github.excu101.vortexfilemanager.ui.theme.themedColorAnimation
import io.github.excu101.vortexfilemanager.ui.util.plus

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
    SurfaceLazyRow(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        elevation = 8.dp,
        contentPadding = contentPaddings,
        color = Theme[trailSurfaceColorKey],
        state = scroller
    ) {
        itemsIndexed(
            items = segments,
            key = { index, item -> item.path.hashCode() }
        ) { i, model ->
            val isSelected = i == currentSelected
            val isLast = i != segments.size - 1

            TrailItemView(
                title = model.name,
                isSelected = isSelected,
                isLast = isLast,
                onTrailClick = { onTrailClick(model) }
            )
        }
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

@Composable
private fun TrailItemView(
    title: String,
    isSelected: Boolean = false,
    isLast: Boolean = false,
    onTrailClick: () -> Unit,
) {
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
                ),
                onClick = onTrailClick
            ),
            text = title,
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
        if (isLast) {
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
