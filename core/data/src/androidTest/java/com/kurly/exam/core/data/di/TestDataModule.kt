package com.kurly.exam.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kurly.exam.core.data.remote.api.SectionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {

    @Provides
    @Singleton
    fun provideSectionApi(retrofit: Retrofit): SectionApi =
        retrofit.create(SectionApi::class.java)

    // Hilt의 생명주기를 넘어 테스트 프로세스 전체에서 DataStore 인스턴스를 단 하나만 유지하기 위함.
    @Volatile
    private var dataStoreInstance: DataStore<Preferences>? = null

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        // 이미 생성된 인스턴스가 있으면 즉시 반환
        return dataStoreInstance ?: synchronized(this) {
            // synchronized 블록 내에서 다시 한번 확인 (double-checked locking)
            dataStoreInstance ?: PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("test_user_preferences") }
            ).also {
                dataStoreInstance = it
            }
        }
    }
}