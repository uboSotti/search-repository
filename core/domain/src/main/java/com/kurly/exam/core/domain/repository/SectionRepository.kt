package com.kurly.exam.core.domain.repository

import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.model.Section

interface SectionRepository {

    suspend fun getSections(page: Int): Result<List<Section>>

    suspend fun getSectionProducts(sectionId: Int): Result<List<Product>>
}
