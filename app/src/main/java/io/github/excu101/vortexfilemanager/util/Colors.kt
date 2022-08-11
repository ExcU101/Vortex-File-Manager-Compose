package io.github.excu101.vortexfilemanager.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.excu101.ui.component.layout.ProgressBarColors
import io.github.excu101.ui.component.layout.ProgressBarColors.Companion.colors
import io.github.excu101.ui.component.layout.SectionListDefaults.SectionListColors
import io.github.excu101.ui.component.layout.SectionListDefaults.colors
import io.github.excu101.vortexfilemanager.ui.theme.Theme
import io.github.excu101.vortexfilemanager.ui.theme.key.*

@Composable
fun drawerColors(
    title: Color = Theme[mainDrawerTitleColorKey],
    background: Color = Theme[mainDrawerBackgroundColorKey],
    itemBackground: Color = Theme[mainDrawerSurfaceColorKey],
    itemBackgroundSelected: Color = Theme[mainDrawerItemSelectedBackgroundColorKey],
    itemTitle: Color = Theme[mainDrawerActionTitleTextColorKey],
    itemTitleSelected: Color = Theme[mainDrawerActionTitleSelectedTextColorKey],
    itemIcon: Color = Theme[mainDrawerActionIconTintColorKey],
    itemIconSelected: Color = Theme[mainDrawerActionIconSelectedTintColorKey],
): SectionListColors = colors(
    title,
    background,
    itemBackground,
    itemBackgroundSelected,
    itemTitle,
    itemTitleSelected,
    itemIcon,
    itemIconSelected
)

@Composable
fun progressBarColors(
    messageColor: Color = Theme[layoutProgressTitleTextColorKey],
    backgroundColor: Color = Theme[layoutProgressBarBackgroundColorKey],
    buttonTextColor: Color = Theme[layoutProgressActionTintColorKey],
    progressColor: Color = Theme[layoutProgressBarTintColorKey],
): ProgressBarColors = colors(
    backgroundColor = backgroundColor,
    buttonTextColor = buttonTextColor,
    barColor = progressColor,
    messageColor = messageColor
)