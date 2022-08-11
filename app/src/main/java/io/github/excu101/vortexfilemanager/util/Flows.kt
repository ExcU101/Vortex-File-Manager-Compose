package io.github.excu101.vortexfilemanager.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow

suspend inline fun <T> MutableStateFlow<T>.emit(block: T.() -> T) {
    emit(value = block(value))
}

suspend inline fun <T> MutableStateFlow<T>.applier(block: T.() -> Unit) {
    val old = value
    val new = old.apply(block)
    emit(value = new)
}

val Context.pathDataStore: DataStore<Preferences> by preferencesDataStore(name = "path_data_store")