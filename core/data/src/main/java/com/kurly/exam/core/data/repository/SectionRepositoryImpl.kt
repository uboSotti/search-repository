package com.kurly.exam.core.data.repository

import com.kurly.exam.core.common.dispatcher.DispatcherProvider
import com.kurly.exam.core.data.remote.api.SectionApi
import com.kurly.exam.core.data.remote.dto.ProductDto
import com.kurly.exam.core.data.remote.dto.SectionDto
import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.domain.repository.SectionRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val sectionApi: SectionApi,
    private val dispatcherProvider: DispatcherProvider
) : SectionRepository {

    override suspend fun getSections(page: Int): Result<List<Section>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                sectionApi.getSections(page).data.map { it.toDomain() }
            }
        }

    override suspend fun getSectionProducts(sectionId: Int): Result<List<Product>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                sectionApi.getSectionProducts(sectionId).data.map { it.toDomain() }
            }
        }
}

// Mapper functions
fun SectionDto.toDomain(): Section = Section(
    title = title,
    id = id,
    type = type,
    url = url
)

fun ProductDto.toDomain(): Product = Product(
    id = id,
    name = name,
    image = image,
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    isSoldOut = isSoldOut
)
