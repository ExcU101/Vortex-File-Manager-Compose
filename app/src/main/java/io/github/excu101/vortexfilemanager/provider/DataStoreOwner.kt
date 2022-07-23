package io.github.excu101.vortexfilemanager.provider

interface DataStoreOwner {

    suspend fun readString(key: String): String?

    suspend fun writeString(key: String, value: String)

}