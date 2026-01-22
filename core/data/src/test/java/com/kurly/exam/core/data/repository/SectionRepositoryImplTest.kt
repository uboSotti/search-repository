package com.kurly.exam.core.data.repository

import com.kurly.exam.core.common.dispatcher.DispatcherProvider
import com.kurly.exam.core.data.remote.api.SectionApi
import com.kurly.exam.core.data.remote.dto.PagingDto
import com.kurly.exam.core.data.remote.dto.ProductDto
import com.kurly.exam.core.data.remote.dto.SectionDto
import com.kurly.exam.core.data.remote.dto.SectionProductsResponse
import com.kurly.exam.core.data.remote.dto.SectionsResponse
import com.kurly.exam.core.domain.repository.SectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SectionRepositoryImpl 단위 테스트")
internal class SectionRepositoryImplTest {

    private lateinit var sectionApi: SectionApi
    private lateinit var repository: SectionRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @BeforeEach
    fun setUp() {
        sectionApi = mockk()
        // 테스트용 DispatcherProvider
        dispatcherProvider = object : DispatcherProvider {
            override val main: CoroutineDispatcher = StandardTestDispatcher()
            override val io: CoroutineDispatcher = StandardTestDispatcher()
            override val default: CoroutineDispatcher = StandardTestDispatcher()
        }
        
        repository = SectionRepositoryImpl(sectionApi, dispatcherProvider)
    }

    @Test
    @DisplayName("getSections 호출 시 API 데이터를 도메인 모델로 변환하여 반환한다")
    fun `getSections should map dto to domain model`() = runTest(dispatcherProvider.io) {
        // Given
        val sectionDto = SectionDto("Title", 1, "type", "url")
        val response = SectionsResponse(
            data = listOf(sectionDto),
            paging = PagingDto(2)
        )
        coEvery { sectionApi.getSections(1) } returns response

        // When
        val result = repository.getSections(1)

        // Then
        assertTrue(result.isSuccess)
        val sections = result.getOrNull()
        assertEquals(1, sections?.size)
        assertEquals("Title", sections?.first()?.title)
    }

    @Test
    @DisplayName("getSectionProducts 호출 시 API 데이터를 도메인 모델로 변환하여 반환한다")
    fun `getSectionProducts should map dto to domain model`() = runTest(dispatcherProvider.io) {
        // Given
        val productDto = ProductDto(1, "Name", "Image", 1000, null, false)
        val response = SectionProductsResponse(data = listOf(productDto))
        coEvery { sectionApi.getSectionProducts(1) } returns response

        // When
        val result = repository.getSectionProducts(1)

        // Then
        assertTrue(result.isSuccess)
        val products = result.getOrNull()
        assertEquals(1, products?.size)
        assertEquals("Name", products?.first()?.name)
    }
}
