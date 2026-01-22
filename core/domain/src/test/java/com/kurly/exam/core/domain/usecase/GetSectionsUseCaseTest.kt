package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.domain.repository.SectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetSectionsUseCase 단위 테스트")
internal class GetSectionsUseCaseTest {

    private lateinit var sectionRepository: SectionRepository
    private lateinit var getSectionsUseCase: GetSectionsUseCase

    @BeforeEach
    fun setUp() {
        sectionRepository = mockk()
        getSectionsUseCase = GetSectionsUseCase(sectionRepository)
    }

    @Test
    @DisplayName("섹션 목록을 성공적으로 불러오는지 확인")
    fun `invoke should return success result when repository returns data`() = runTest {
        // Given
        val expectedSections = listOf(
            Section("Title 1", 1, "grid", "url1"),
            Section("Title 2", 2, "horizontal", "url2")
        )
        coEvery { sectionRepository.getSections(1) } returns Result.success(expectedSections)

        // When
        val result = getSectionsUseCase(1)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedSections, result.getOrNull())
    }

    @Test
    @DisplayName("에러 발생 시 실패 결과를 반환하는지 확인")
    fun `invoke should return failure result when repository throws error`() = runTest {
        // Given
        val exception = Exception("Network Error")
        coEvery { sectionRepository.getSections(1) } returns Result.failure(exception)

        // When
        val result = getSectionsUseCase(1)

        // Then
        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
