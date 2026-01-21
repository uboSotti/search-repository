package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.SectionRepository
import javax.inject.Inject

class GetSectionsUseCase @Inject constructor(
    private val sectionRepository: SectionRepository
) {
    suspend operator fun invoke(page: Int) = sectionRepository.getSections(page)
}
