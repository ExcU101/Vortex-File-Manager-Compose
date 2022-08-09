package io.github.excu101.ui.component.icon

import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

val Unspecified: ImageVector
    get() = materialIcon(name = "Unspecified") {
        ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportHeight = 24F,
            viewportWidth = 24F
        )
    }

@OptIn(ExperimentalContracts::class)
fun ImageVector?.isUnspecified(): Boolean {
    contract {
        returns(value = false) implies (this@isUnspecified != null)
    }
    return this == null || this == Unspecified
}

@OptIn(ExperimentalContracts::class)
fun ImageVector?.isNotUnspecified(): Boolean {
    contract {
        returns(value = true) implies (this@isNotUnspecified != null)
    }
    return this != Unspecified
}