package io.github.excu101.vortexfilemanager.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.excu101.vortexfilemanager.provider.AndroidFileProvider
import io.github.excu101.vortexfilemanager.provider.DataStoreOwner
import io.github.excu101.vortexfilemanager.provider.DataStoreOwnerImpl
import io.github.excu101.vortexfilemanager.provider.SettingProperties
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideAndroidFileProvider(
        @ApplicationContext context: Context,
    ): AndroidFileProvider {
        return AndroidFileProvider(context)
    }

    @Provides
    @Singleton
    fun provideSettingProperties(
        @ApplicationContext context: Context,
    ): SettingProperties {
        return SettingProperties(context = context)
    }

    @Provides
    @Singleton
    fun provideDataStoreOwner(@ApplicationContext context: Context): DataStoreOwner {
        return DataStoreOwnerImpl(context)
    }

}