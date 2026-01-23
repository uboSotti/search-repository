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

class SectionPagingSource(
    private val sectionApi: SectionApi
) : PagingSource<Int, SectionWithProducts>() {

    override fun getRefreshKey(state: PagingState<Int, SectionWithProducts>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

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
