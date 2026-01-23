package com.kurly.exam.core.domain.repository

import androidx.paging.PagingData
import com.kurly.exam.core.domain.model.SectionWithProducts
import com.kurly.exam.core.model.Product
import com.kurly.exam.core.model.Section
import kotlinx.coroutines.flow.Flow

/**
 * 섹션 및 상품 데이터를 관리하는 리포지토리 인터페이스.
 */
interface SectionRepository {

    /**
     * 지정된 페이지의 섹션 목록을 가져옵니다.
     *
     * @param page 가져올 페이지 번호.
     * @return 성공 시 [Section] 리스트를 담은 [Result], 실패 시 에러를 담은 [Result].
     */
    suspend fun getSections(page: Int): Result<List<Section>>

    /**
     * 지정된 섹션 ID의 상품 목록을 가져옵니다.
     *
     * @param sectionId 상품 목록을 가져올 섹션의 ID.
     * @return 성공 시 [Product] 리스트를 담은 [Result], 실패 시 에러를 담은 [Result].
     */
    suspend fun getSectionProducts(sectionId: Int): Result<List<Product>>

    /**
     * 섹션과 상품 목록을 페이징하여 가져옵니다.
     *
     * @return [PagingData<SectionWithProducts>]를 방출하는 [Flow].
     */
    fun getSectionWithProductsPaged(): Flow<PagingData<SectionWithProducts>>
}
