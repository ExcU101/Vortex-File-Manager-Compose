package io.github.excu101.vortexfilemanager.provider

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.excu101.vortexfilemanager.util.pathDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreOwnerImpl @Inject constructor(
    private val context: Context
) : DataStoreOwner {

    @Suppress("ReplaceGetOrSet")
    override suspend fun readString(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        return context.pathDataStore.data.first().get(prefKey)
    }

    override suspend fun writeString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.pathDataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }
}