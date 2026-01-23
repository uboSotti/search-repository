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

/**
 * Repository 인터페이스와 구현체를 바인딩하는 Hilt 모듈.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * [SectionRepository]의 구현체로 [SectionRepositoryImpl]을 바인딩합니다.
     */
    @Binds
    @Singleton
    abstract fun bindSectionRepository(
        sectionRepositoryImpl: SectionRepositoryImpl
    ): SectionRepository

    /**
     * [FavoriteRepository]의 구현체로 [FavoriteRepositoryImpl]을 바인딩합니다.
     */
    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}