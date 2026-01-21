package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.SectionRepository
import javax.inject.Inject

class GetSectionProductsUseCase @Inject constructor(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(sectionId: Int) = sectionRepository.getSectionProducts(sectionId)
}
