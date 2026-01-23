package com.kurly.exam.core.data.remote.api

import com.kurly.exam.core.data.remote.dto.SectionProductsResponse
import com.kurly.exam.core.data.remote.dto.SectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 섹션 및 상품 관련 API를 정의하는 Retrofit 인터페이스
 */
interface SectionApi {

    /**
     * 지정된 페이지의 섹션 목록을 가져옵니다.
     *
     * @param page 가져올 페이지 번호
     * @return [SectionsResponse] 섹션 목록을 포함하는 응답 객체
     */
    @GET("sections")
    suspend fun getSections(
        @Query("page") page: Int
    ): SectionsResponse

    /**
     * 지정된 섹션 ID에 해당하는 상품 목록을 가져옵니다.
     *
     * @param sectionId 상품 목록을 가져올 섹션의 ID
     * @return [SectionProductsResponse] 상품 목록을 포함하는 응답 객체
     */
    @GET("section/products")
    suspend fun getSectionProducts(
        @Query("sectionId") sectionId: Int
    ): SectionProductsResponse
}
