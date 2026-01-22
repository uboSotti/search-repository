package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.repository.SectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetSectionProductsUseCase 단위 테스트")
internal class GetSectionProductsUseCaseTest {

    private lateinit var sectionRepository: SectionRepository
    private lateinit var getSectionProductsUseCase: GetSectionProductsUseCase

    @BeforeEach
    fun setUp() {
        sectionRepository = mockk()
        getSectionProductsUseCase = GetSectionProductsUseCase(sectionRepository)
    }

    @Test
    @DisplayName("섹션 별 상품 목록을 성공적으로 불러오는지 확인")
    fun `invoke should return success result when repository returns data`() = runTest {
        // Given
        val expectedProducts = listOf(
            Product(1, "Product 1", "image1", 1000, 900, false),
            Product(2, "Product 2", "image2", 2000, null, true)
        )
        coEvery { sectionRepository.getSectionProducts(1) } returns Result.success(expectedProducts)

        // When
        val result = getSectionProductsUseCase(1)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedProducts, result.getOrNull())
    }

    @Test
    @DisplayName("에러 발생 시 실패 결과를 반환하는지 확인")
    fun `invoke should return failure result when repository throws error`() = runTest {
        // Given
        val exception = Exception("Network Error")
        coEvery { sectionRepository.getSectionProducts(1) } returns Result.failure(exception)

        // When
        val result = getSectionProductsUseCase(1)

        // Then
        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
