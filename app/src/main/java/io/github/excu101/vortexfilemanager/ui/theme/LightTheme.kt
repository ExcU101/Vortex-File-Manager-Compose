package io.github.excu101.vortexfilemanager.ui.theme

import androidx.compose.ui.graphics.Color
import io.github.excu101.vortexfilemanager.ui.theme.key.*

fun defaultLightTheme(): Theme {
    Theme[backgroundColorKey] = Color.White
    Theme[surfaceColorKey] = Color.White
    Theme[accentColorKey] = Color(color = 0xFF3062FF)

    Theme[mainBarSurfaceColorKey] = Color.White
    Theme[mainBarNavigationIconTintColorKey] = Color.Black
    Theme[mainBarActionIconTintColorKey] = Color.Black
    Theme[mainBarTitleTextColorKey] = Color.Black
    Theme[mainBarSubtitleTextColorKey] = Color.Gray

    Theme[mainBarSurfaceContextualColorKey] = Color(color = 0xFF212121)
    Theme[mainBarActionIconContextualTintColorKey] = Color.White
    Theme[mainBarNavigationIconContextualTintColorKey] = Color.White
    Theme[mainBarTitleContextualTextColorKey] = Color.White

    Theme[mainDrawerSurfaceColorKey] = Color.White
    Theme[mainDrawerActionIconTintColorKey] = Color.Black
    Theme[mainDrawerActionTitleTextColorKey] = Color.Black

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(color = 0x4D3062FF)
    Theme[mainDrawerActionIconSelectedTintColorKey] = Color(color = 0xFF3062FF)
    Theme[mainDrawerActionTitleSelectedTextColorKey] = Color(color = 0xFF3062FF)

    Theme[fileAdditionalSurfaceColorKey] = Color.White
    Theme[fileAdditionalTitleTextColorKey] = Color.Black
    Theme[fileAdditionalActionIconTintColorKey] = Color.Black

    Theme[trailSurfaceColorKey] = Color.White
    Theme[trailItemTitleTextColorKey] = Color.Black
    Theme[trailItemArrowTintColorKey] = Color.Black

    Theme[trailItemTitleSelectedTextColorKey] = Color(color = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] = Color(color = 0xFF3062FF)

    Theme[fileWarningBackgroundColorKey] = Color.White
    Theme[fileWarningIconTintColorKey] = Color.Black
    Theme[fileWarningTitleTextColorKey] = Color.Black
    Theme[fileWarningActionContentColorKey] = Color(color = 0xFF3062FF)

    Theme[fileItemSurfaceColorKey] = Color.White
    Theme[fileItemTitleTextColorKey] = Color.Black
    Theme[fileItemSecondaryTextColorKey] = Color.DarkGray
    Theme[fileItemIndexTextColorKey] = Color.Gray
    Theme[fileItemIconTintColorKey] = Color.White
    Theme[fileItemIconBackgroundColorKey] = Color.DarkGray

    Theme[fileItemSurfaceSelectedColorKey] = Color(color = 0x4D2962FF)
    Theme[fileItemTitleSelectedTextColorKey] = Color(color = 0xFF3062FF)
    Theme[fileItemSecondarySelectedTextColorKey] = Color(color = 0xFF3062FF)
    Theme[fileItemIconSelectedTintColorKey] = Color(color = 0xFF3062FF)
    Theme[fileItemIconBackgroundSelectedColorKey] = Color.LightGray

    Theme[layoutProgressBarTintColorKey] = Color(color = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.Black
    Theme[layoutProgressActionTintColorKey] = Color.Black

    return Theme
}