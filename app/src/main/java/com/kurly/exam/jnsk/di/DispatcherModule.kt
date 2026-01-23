package com.kurly.exam.jnsk.di

import com.kurly.exam.core.common.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Coroutine Dispatcher를 제공하는 Hilt 모듈.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    /**
     * 앱 전체에서 사용할 [DispatcherProvider]를 제공합니다.
     *
     * @return [DefaultDispatcherProvider] 인스턴스
     */
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}
