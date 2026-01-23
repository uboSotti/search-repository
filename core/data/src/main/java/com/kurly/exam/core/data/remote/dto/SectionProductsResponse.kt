package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 특정 섹션의 상품 목록을 포함하는 API 응답 데이터 전송 객체(DTO).
 *
 * @property data 상품 목록([ProductDto])을 담고 있는 리스트.
 */
@Serializable
data class SectionProductsResponse(
    @SerialName("data")
    val data: List<ProductDto>
)
