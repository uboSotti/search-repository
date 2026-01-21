package com.kurly.exam.core.data.remote.api

import com.kurly.exam.core.data.remote.dto.SectionProductsResponse
import com.kurly.exam.core.data.remote.dto.SectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SectionApi {

    @GET("sections")
    suspend fun getSections(
        @Query("page") page: Int
    ): SectionsResponse

    @GET("section/products")
    suspend fun getSectionProducts(
        @Query("sectionId") sectionId: Int
    ): SectionProductsResponse
}
