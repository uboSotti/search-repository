package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.SectionRepository
import javax.inject.Inject

/**
 * 지정된 페이지의 섹션 목록을 가져오는 유즈케이스.
 *
 * @param sectionRepository 섹션 데이터를 제공하는 리포지토리.
 */
class GetSectionsUseCase @Inject constructor(
    private val sectionRepository: SectionRepository
) {
    /**
     * 유즈케이스를 실행하여 섹션 목록을 가져옵니다.
     *
     * @param page 가져올 페이지 번호.
     * @return [sectionRepository.getSections]의 결과.
     */
    suspend operator fun invoke(page: Int) = sectionRepository.getSections(page)
}
