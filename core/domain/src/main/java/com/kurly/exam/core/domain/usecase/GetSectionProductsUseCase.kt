package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.SectionRepository
import javax.inject.Inject

/**
 * 지정된 섹션 ID의 상품 목록을 가져오는 유즈케이스.
 *
 * @param sectionRepository 섹션 데이터를 제공하는 리포지토리.
 */
class GetSectionProductsUseCase @Inject constructor(
    private val sectionRepository: SectionRepository
) {
    /**
     * 유즈케이스를 실행하여 특정 섹션의 상품 목록을 가져옵니다.
     *
     * @param sectionId 상품 목록을 가져올 섹션의 ID.
     * @return [sectionRepository.getSectionProducts]의 결과.
     */
    suspend operator fun invoke(sectionId: Int) = sectionRepository.getSectionProducts(sectionId)
}
