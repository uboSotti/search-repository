package com.kurly.exam.core.data.di

import com.kurly.exam.core.data.repository.FavoriteRepositoryImpl
import com.kurly.exam.core.data.repository.SectionRepositoryImpl
import com.kurly.exam.core.domain.repository.FavoriteRepository
import com.kurly.exam.core.domain.repository.SectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSectionRepository(
        sectionRepositoryImpl: SectionRepositoryImpl
    ): SectionRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}