package io.github.excu101.vortexfilemanager.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow

suspend inline fun <T> MutableStateFlow<T>.emit(block: T.() -> T) {
    emit(value = block(value))
}

val Context.pathDataStore: DataStore<Preferences> by preferencesDataStore(name = "path_data_store")