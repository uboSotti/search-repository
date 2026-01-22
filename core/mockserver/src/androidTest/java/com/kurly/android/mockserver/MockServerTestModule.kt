package com.kurly.android.mockserver

import com.kurly.android.mockserver.core.AssetFileProvider
import com.kurly.android.mockserver.core.FileProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockServerTestModule {

    @Provides
    @Singleton
    fun provideFileProvider(assetFileProvider: AssetFileProvider): FileProvider = assetFileProvider
}
