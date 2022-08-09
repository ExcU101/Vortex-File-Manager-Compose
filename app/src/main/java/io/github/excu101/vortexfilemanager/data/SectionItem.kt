package io.github.excu101.vortexfilemanager.data

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.RawValue

data class SectionItem(
    val name: String,
    val icon: @RawValue ImageVector,
    override var isSelected: Boolean
) : SelectableValue