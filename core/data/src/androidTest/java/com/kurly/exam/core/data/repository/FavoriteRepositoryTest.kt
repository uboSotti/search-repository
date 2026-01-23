package com.kurly.exam.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kurly.exam.core.domain.repository.FavoriteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FavoriteRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: FavoriteRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences> // 테스트에서 파일 정리를 위해 주입 받음

    private lateinit var context: Context

    // DataStore 파일 이름 (TestDataModule.kt에서 정의된 이름과 일치해야 합니다)
    private val TEST_DATASTORE_NAME = "test_user_preferences"


    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    /**
     * DataStore 파일 잠금으로 인한 충돌을 방지하기 위해,
     * 테스트가 끝날 때마다 데이터 내용을 지우고, DataStore 파일을 직접 삭제합니다.
     */
    @After
    fun tearDown() = runTest {
        // 1. DataStore 내용 비우기 (데이터 정리)
        dataStore.edit { it.clear() }

        // 2. DataStore 파일 직접 삭제 (파일 잠금 해제 유도)
        val dataStoreFile = context.preferencesDataStoreFile(TEST_DATASTORE_NAME)
        if (dataStoreFile.exists()) {
            dataStoreFile.delete()
        }
    }

    @Test
    fun toggleFavorite_adds_id_when_it_does_not_exist() = runTest {
        // Given
        val productId = 101
        assertTrue("초기 상태는 비어있어야 합니다", repository.getFavoriteIds().first().isEmpty())

        // When
        repository.toggleFavorite(productId)

        // Then
        val favoriteIds = repository.getFavoriteIds().first()
        assertTrue("찜 목록에 상품 ID가 포함되어야 합니다", favoriteIds.contains(productId))
    }

    @Test
    fun toggleFavorite_removes_id_when_it_already_exists() = runTest {
        // Given
        val productId = 202
        repository.toggleFavorite(productId) // 먼저 추가
        assertTrue("추가 후 ID가 포함되어야 합니다", repository.getFavoriteIds().first().contains(productId))

        // When
        repository.toggleFavorite(productId) // 다시 토글 (제거)

        // Then
        val favoriteIds = repository.getFavoriteIds().first()
        assertFalse("찜 목록에서 상품 ID가 제거되어야 합니다", favoriteIds.contains(productId))
    }

    @Test
    fun multiple_favorites_are_stored_correctly() = runTest {
        // Given
        val ids = setOf(1, 2, 3)

        // When
        ids.forEach { repository.toggleFavorite(it) }

        // Then
        val favoriteIds = repository.getFavoriteIds().first()
        assertEquals("찜한 상품 개수가 일치해야 합니다", ids.size, favoriteIds.size)
        assertEquals("모든 상품 ID가 포함되어야 합니다", ids, favoriteIds)
    }
}