package com.kurly.exam.feature.main

import app.cash.turbine.test
import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.domain.usecase.GetSectionsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("MainViewModel 단위 테스트")
internal class MainViewModelTest {

    private lateinit var getSectionsUseCase: GetSectionsUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getSectionsUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("초기화 시 섹션 목록을 불러오고 성공 상태를 업데이트한다")
    fun `init should fetch sections and update state to success`() = runTest {
        // Given
        val sections = listOf(Section("Title", 1, "type", "url"))
        coEvery { getSectionsUseCase(1) } returns Result.success(sections)

        // When
        viewModel = MainViewModel(getSectionsUseCase)

        // Then
        viewModel.uiState.test {
            // 초기 상태 (isLoading = true) - MainViewModel init 블록에서 실행됨
            // Turbine은 구독 시작 시점의 최신 상태를 먼저 방출할 수도 있으므로, 
            // 흐름에 따라 awaitItem()을 적절히 호출해야 합니다.
            
            // StateFlow는 초기값을 가지고 시작하므로, 구독 시점의 값(첫 번째 awaitItem)을 확인합니다.
            // 여기서는 init {} 블록이 코루틴을 실행하므로, 타이밍 이슈가 있을 수 있습니다.
            // runTest와 StandardTestDispatcher를 사용하면 코루틴 실행을 제어할 수 있습니다.
            
            // 첫 번째 값은 초기값 (isLoading=true)
            val initialItem = awaitItem()
            assertTrue(initialItem.isLoading)
            
            // 코루틴 스케줄러가 실행되도록 양보
            testDispatcher.scheduler.advanceUntilIdle()

            // 두 번째 값은 성공 결과 (isLoading=false, sections=...)
            val successItem = awaitItem()
            assertFalse(successItem.isLoading)
            assertEquals(sections, successItem.sections)
        }
    }

    @Test
    @DisplayName("섹션 불러오기 실패 시 에러 메시지를 업데이트한다")
    fun `fetchSections should update state with error message on failure`() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery { getSectionsUseCase(1) } returns Result.failure(Exception(errorMessage))

        // When
        viewModel = MainViewModel(getSectionsUseCase)

        // Then
        viewModel.uiState.test {
            val initialItem = awaitItem()
            assertTrue(initialItem.isLoading)
            
            testDispatcher.scheduler.advanceUntilIdle()

            val errorItem = awaitItem()
            assertFalse(errorItem.isLoading)
            assertEquals(errorMessage, errorItem.errorMessage)
        }
    }
}
