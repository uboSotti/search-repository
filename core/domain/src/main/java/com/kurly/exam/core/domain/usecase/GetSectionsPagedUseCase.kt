package com.kurly.exam.core.domain.usecase

import androidx.paging.PagingData
import com.kurly.exam.core.domain.model.SectionWithProducts
import com.kurly.exam.core.domain.repository.SectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 섹션과 상품 목록을 페이징하여 가져오는 유즈케이스.
 *
 * @param sectionRepository 섹션 데이터를 제공하는 리포지토리.
 */
class GetSectionsPagedUseCase @Inject constructor(
    private val sectionRepository: SectionRepository
) {
    /**
     * 유즈케이스를 실행하여 페이징된 섹션 및 상품 데이터를 가져옵니다.
     *
     * @return [PagingData<SectionWithProducts>]를 방출하는 [Flow].
     */
    operator fun invoke(): Flow<PagingData<SectionWithProducts>> =
        sectionRepository.getSectionWithProductsPaged()
}
