package io.github.excu101.vortexfilemanager.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection.Ltr

operator fun PaddingValues.plus(values: PaddingValues): PaddingValues {
    return PaddingValues(
        top = calculateTopPadding() + values.calculateTopPadding(),
        start = calculateStartPadding(Ltr) + values.calculateStartPadding(Ltr),
        end = calculateEndPadding(Ltr) + values.calculateEndPadding(Ltr),
        bottom = calculateBottomPadding() + values.calculateBottomPadding()
    )
}