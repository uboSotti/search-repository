package com.kurly.exam.core.data.di

import com.kurly.exam.core.data.remote.api.SectionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSectionApi(retrofit: Retrofit): SectionApi =
        retrofit.create(SectionApi::class.java)
}
