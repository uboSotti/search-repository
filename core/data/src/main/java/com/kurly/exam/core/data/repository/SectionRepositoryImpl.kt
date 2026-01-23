package com.kurly.exam.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kurly.exam.core.common.dispatcher.DispatcherProvider
import com.kurly.exam.core.data.mapper.toDomain
import com.kurly.exam.core.data.paging.SectionPagingSource
import com.kurly.exam.core.data.remote.api.SectionApi
import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.model.Section
import com.kurly.exam.core.domain.model.SectionWithProducts
import com.kurly.exam.core.domain.repository.SectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val sectionApi: SectionApi,
    private val dispatcherProvider: DispatcherProvider
) : SectionRepository {

    override suspend fun getSections(page: Int): Result<List<Section>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                sectionApi.getSections(page).data.map { it.toDomain() }
            }
        }

    override suspend fun getSectionProducts(sectionId: Int): Result<List<Product>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                sectionApi.getSectionProducts(sectionId).data.map { it.toDomain() }
            }
        }

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
