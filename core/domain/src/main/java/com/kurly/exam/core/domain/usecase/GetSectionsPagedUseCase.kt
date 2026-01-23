package com.kurly.exam.core.domain.usecase

import androidx.paging.PagingData
import com.kurly.exam.core.domain.model.SectionWithProducts
import com.kurly.exam.core.domain.repository.SectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSectionsPagedUseCase @Inject constructor(
    private val sectionRepository: SectionRepository
) {
    operator fun invoke(): Flow<PagingData<SectionWithProducts>> =
        sectionRepository.getSectionWithProductsPaged()
}
