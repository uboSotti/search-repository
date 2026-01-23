package com.kurly.exam.core.domain.repository

import androidx.paging.PagingData
import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.domain.model.SectionWithProducts
import kotlinx.coroutines.flow.Flow

interface SectionRepository {

    suspend fun getSections(page: Int): Result<List<Section>>

    suspend fun getSectionProducts(sectionId: Int): Result<List<Product>>

    fun getSectionWithProductsPaged(): Flow<PagingData<SectionWithProducts>>
}
