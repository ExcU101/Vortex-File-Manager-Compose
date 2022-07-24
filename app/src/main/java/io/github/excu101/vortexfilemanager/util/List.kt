package io.github.excu101.vortexfilemanager.util

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import io.github.excu101.pluginsystem.model.Action

fun <T> SavedStateHandle.get(key: String, defValue: T): T {
    return get<T>(key) ?: defValue
}

interface ListBuilder<T> {

    val list: List<T>

    fun item(value: T)

    fun items(iterable: Iterable<T>)

}

inline fun <T> List<T>.onEmpty(
    nonEmpty: List<T>.() -> Unit = {},
    empty: List<T>.() -> Unit
): List<T> {
    if (isEmpty()) {
        empty(this)
    } else {
        nonEmpty(this)
    }
    return this
}

fun <T> ListBuilder<T>.item(block: () -> T) = item(block.invoke())

fun ListBuilder<Action>.item(title: String, icon: ImageVector) =
    item(Action(title = title, icon = icon))

fun <T, E, B : Collection<E>> ListBuilder<Pair<T, B>>.item(
    first: T,
    second: ListBuilder<E>.() -> Unit
) {
    item(first to listBuilder(second) as B)
}

inline fun <T> listBuilder(block: ListBuilder<T>.() -> Unit): List<T> {
    return object : ListBuilder<T> {
        private val _list = mutableListOf<T>()

        override val list: List<T>
            get() = _list

        override fun item(value: T) {
            _list.add(value)
        }

        override fun items(iterable: Iterable<T>) {
            _list.addAll(iterable)
        }
    }.apply(block).list
}

open class MapSet<K, V> : AbstractMutableSet<V> {

    protected val keyExtractor: (V) -> K

    protected val map: MutableMap<K, V>

    constructor(keyExtractor: (V) -> K) : this(keyExtractor, false)

    constructor(keyExtractor: (V) -> K, isLinked: Boolean) : super() {
        this.keyExtractor = keyExtractor
        map = if (isLinked) linkedMapOf() else mutableMapOf()
    }

    operator fun get(key: K) = map[key]

    open operator fun plus(element: V) = MapSet(keyExtractor).apply {
        addAll(elements = this)
        add(element)
    }

    override fun add(element: V): Boolean {
        return map.put(keyExtractor(element), element) == null
    }

    override fun remove(element: V): Boolean {
        return map.remove(keyExtractor(element)) != null
    }

    override fun iterator(): MutableIterator<V> {
        return map.values.iterator()
    }

    override fun contains(element: V): Boolean {
        return map.containsKey(keyExtractor(element))
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun clear() {
        map.clear()
    }

    override val size: Int
        get() = map.size

}