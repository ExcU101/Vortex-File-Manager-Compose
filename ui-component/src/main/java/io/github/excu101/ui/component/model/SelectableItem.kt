package io.github.excu101.ui.component.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

@Immutable
data class SelectableItem<T>(
    val isSelected: MutableState<Boolean>,
    val value: T
) {

    constructor(isSelected: Boolean, value: T) : this(mutableStateOf(isSelected), value)

}