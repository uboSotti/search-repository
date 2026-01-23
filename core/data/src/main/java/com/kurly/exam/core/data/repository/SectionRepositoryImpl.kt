package com.kurly.exam.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kurly.exam.core.common.dispatcher.DispatcherProvider
import com.kurly.exam.core.data.mapper.toDomain
import com.kurly.exam.core.data.paging.SectionPagingSource
import com.kurly.exam.core.data.remote.api.SectionApi
import com.kurly.exam.core.domain.model.SectionWithProducts
import com.kurly.exam.core.domain.repository.SectionRepository
import com.kurly.exam.core.model.Product
import com.kurly.exam.core.model.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [SectionRepository]의 구현체.
 * API로부터 섹션 및 상품 데이터를 가져옵니다.
 *
 * @param sectionApi Retrofit을 통해 생성된 API 서비스.
 * @param dispatcherProvider 코루틴 디스패처 제공자.
 */
class SectionRepositoryImpl @Inject constructor(
    private val sectionApi: SectionApi,
    private val dispatcherProvider: DispatcherProvider
) : SectionRepository {

    /**
     * 지정된 페이지의 섹션 목록을 가져옵니다.
     *
     * @param page 가져올 페이지 번호.
     * @return 성공 시 [Section] 리스트를 담은 [Result], 실패 시 에러를 담은 [Result].
     */
    override suspend fun getSections(page: Int): Result<List<Section>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                sectionApi.getSections(page).data.map { it.toDomain() }
            }
        }

    /**
     * 지정된 섹션 ID의 상품 목록을 가져옵니다.
     *
     * @param sectionId 상품 목록을 가져올 섹션의 ID.
     * @return 성공 시 [Product] 리스트를 담은 [Result], 실패 시 에러를 담은 [Result].
     */
    override suspend fun getSectionProducts(sectionId: Int): Result<List<Product>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                sectionApi.getSectionProducts(sectionId).data.map { it.toDomain() }
            }
        }

    /**
     * 섹션과 상품 목록을 페이징하여 가져옵니다.
     * Paging3 라이브러리를 사용하여 구현되었습니다.
     *
     * @return [PagingData<SectionWithProducts>]를 방출하는 [Flow].
     */
    override fun getSectionWithProductsPaged(): Flow<PagingData<SectionWithProducts>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // 페이지 크기는 API 특성에 맞게 조정 (임의 설정)
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SectionPagingSource(sectionApi) }
        ).flow
    }
}
