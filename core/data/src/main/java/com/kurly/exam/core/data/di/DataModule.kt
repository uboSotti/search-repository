package com.kurly.exam.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kurly.exam.core.data.remote.api.SectionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 데이터 소스와 관련된 의존성을 제공하는 Hilt 모듈.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * [SectionApi] 인스턴스를 제공합니다.
     *
     * @param retrofit Retrofit 인스턴스
     * @return [SectionApi]의 구현체
     */
    @Provides
    @Singleton
    fun provideSectionApi(retrofit: Retrofit): SectionApi =
        retrofit.create(SectionApi::class.java)

    /**
     * [DataStore<Preferences>] 인스턴스를 제공합니다.
     * "user_preferences" 라는 이름의 파일에 사용자 환경설정을 저장합니다.
     *
     * @param context 애플리케이션 컨텍스트
     * @return [DataStore<Preferences>] 인스턴스
     */
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("user_preferences") }
        )
    }
}