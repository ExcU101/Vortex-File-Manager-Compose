package io.github.excu101.vortexfilemanager.data

import androidx.compose.runtime.mutableStateListOf
import javax.annotation.concurrent.Immutable


@Immutable
data class SectionGroup(
    val name: String,
) {

    private val _items = mutableStateListOf<SectionItem>()
    val items: List<SectionItem>
        get() = _items

    fun select(index: Int) {
        _items[index].isSelected = true
    }

    fun select(name: String) {
        _items.forEachIndexed { i, it ->
            if (it.name == name) {
                it.isSelected = true
                _items[i] = it
            }
        }
    }
}