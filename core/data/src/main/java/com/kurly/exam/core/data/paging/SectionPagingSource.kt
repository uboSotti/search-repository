package com.kurly.exam.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kurly.exam.core.data.mapper.toDomain
import com.kurly.exam.core.data.remote.api.SectionApi
import com.kurly.exam.core.domain.model.SectionWithProducts
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

/**
 * 섹션 및 관련 상품 목록을 페이징하여 로드하는 [PagingSource] 구현체.
 *
 * @param sectionApi 섹션 및 상품 데이터를 가져오기 위한 API 서비스
 */
class SectionPagingSource(
    private val sectionApi: SectionApi
) : PagingSource<Int, SectionWithProducts>() {

    /**
     * 새로고침 시 현재 페이지 키를 결정합니다.
     */
    override fun getRefreshKey(state: PagingState<Int, SectionWithProducts>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /**
     * 지정된 페이지의 데이터를 비동기적으로 로드합니다.
     * 1. 섹션 목록을 가져옵니다.
     * 2. 각 섹션에 대한 상품 목록을 병렬로 가져옵니다.
     * 3. 섹션과 상품 목록을 결합하여 [SectionWithProducts] 리스트를 생성합니다.
     *
     * @param params 로드할 페이지의 키 및 크기를 포함하는 파라미터
     * @return 로드된 데이터 페이지를 나타내는 [LoadResult]
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SectionWithProducts> {
        return try {
            val page = params.key ?: 1

            coroutineScope {
                // 1. 섹션 목록 호출
                val sectionsResponse = sectionApi.getSections(page)
                val sections = sectionsResponse.data

                // paging 필드가 null일 경우 더 이상 페이지가 없는 것으로 간주
                val nextPage = sectionsResponse.paging?.nextPage

                // 2. 각 섹션별 상품 목록 병렬 호출
                val sectionWithProductsList = sections.map { sectionDto ->
                    async {
                        val productsResponse = try {
                            sectionApi.getSectionProducts(sectionDto.id)
                        } catch (e: Exception) {
                            // 개별 섹션 상품 로딩 실패 시 빈 리스트 처리 또는 에러 처리
                            // 여기서는 빈 리스트로 처리하여 전체 로딩이 실패하지 않도록 함
                            null
                        }

                        val products = productsResponse?.data?.map { it.toDomain() } ?: emptyList()

                        SectionWithProducts(
                            section = sectionDto.toDomain(),
                            products = products
                        )
                    }
                }.awaitAll()

                LoadResult.Page(
                    data = sectionWithProductsList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextPage // null이면 리스트의 끝
                )
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
