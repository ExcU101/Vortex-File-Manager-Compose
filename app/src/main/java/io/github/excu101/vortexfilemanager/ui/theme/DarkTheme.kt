package io.github.excu101.vortexfilemanager.ui.theme

import androidx.compose.ui.graphics.Color
import io.github.excu101.vortexfilemanager.ui.theme.key.*

fun defaultDarkTheme(): Theme {
    Theme[backgroundColorKey] = Color(color = 0xFF212121)
    Theme[surfaceColorKey] = Color.Black
    Theme[accentColorKey] = Color(color = 0xFF3062FF)

    Theme[mainBarSurfaceColorKey] = Color(color = 0xFF212121)
    Theme[mainBarActionIconTintColorKey] = Color.White
    Theme[mainBarNavigationIconTintColorKey] = Color.White
    Theme[mainBarTitleTextColorKey] = Color.White
    Theme[mainBarSubtitleTextColorKey] = Color.LightGray

    Theme[mainBarSurfaceContextualColorKey] = Color(color = 0xFF212121)
    Theme[mainBarActionIconContextualTintColorKey] = Color.White
    Theme[mainBarNavigationIconContextualTintColorKey] = Color.White
    Theme[mainBarTitleContextualTextColorKey] = Color.White

    Theme[mainDrawerTitleColorKey] = Color(0xFF696969)
    Theme[mainDrawerSurfaceColorKey] = Color(color = 0xFF212121)
    Theme[mainDrawerActionIconTintColorKey] = Color.White
    Theme[mainDrawerActionTitleTextColorKey] = Color.White

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(color = 0x4D3062FF)
    Theme[mainDrawerActionIconSelectedTintColorKey] = Color(color = 0xFF3062FF)
    Theme[mainDrawerActionTitleSelectedTextColorKey] = Color(color = 0xFF3062FF)

    Theme[fileAdditionalSurfaceColorKey] = Color(color = 0xFF212121)
    Theme[fileAdditionalTitleTextColorKey] = Color.White
    Theme[fileAdditionalActionIconTintColorKey] = Color.White

    Theme[trailSurfaceColorKey] = Color(color = 0xFF212121)
    Theme[trailItemTitleTextColorKey] = Color.White
    Theme[trailItemArrowTintColorKey] = Color.White

    Theme[trailItemTitleSelectedTextColorKey] = Color(color = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] = Color(color = 0xFF3062FF)

    Theme[fileWarningBackgroundColorKey] = Color(color = 0xFF212121)
    Theme[fileWarningIconTintColorKey] = Color.White
    Theme[fileWarningTitleTextColorKey] = Color.White
    Theme[fileWarningActionContentColorKey] = Color(color = 0xFF3062FF)

    Theme[fileItemSurfaceColorKey] = Color(color = 0xFF212121)
    Theme[fileItemTitleTextColorKey] = Color.White
    Theme[fileItemSecondaryTextColorKey] = Color.LightGray
    Theme[fileItemIndexTextColorKey] = Color.Gray
    Theme[fileItemIconTintColorKey] = Color.White
    Theme[fileItemIconBackgroundColorKey] = Color.Gray

    Theme[fileItemSurfaceSelectedColorKey] = Color(color = 0x4D3062FF)
    Theme[fileItemTitleSelectedTextColorKey] = Color(color = 0xFF3062FF)
    Theme[fileItemSecondarySelectedTextColorKey] = Color(color = 0xFF3062FF)
    Theme[fileItemIconSelectedTintColorKey] = Color(color = 0xFF3062FF)
    Theme[fileItemIconBackgroundSelectedColorKey] = Color.DarkGray

    Theme[layoutProgressBarBackgroundColorKey] = Color.White
    Theme[layoutProgressBarTintColorKey] = Color(color = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.White
    Theme[layoutProgressActionTintColorKey] = Color.White

    return Theme
}