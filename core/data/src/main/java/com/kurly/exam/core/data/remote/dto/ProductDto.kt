package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API로부터 수신하는 상품 데이터 전송 객체(DTO).
 *
 * @property id 상품의 고유 식별자.
 * @property name 상품의 이름.
 * @property image 상품 이미지의 URL.
 * @property originalPrice 상품의 원래 가격.
 * @property discountedPrice 할인된 가격. 할인이 없는 경우 null일 수 있습니다.
 * @property isSoldOut 상품의 품절 여부.
 */
@Serializable
data class ProductDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image") val image: String,
    @SerialName("originalPrice") val originalPrice: Int,
    @SerialName("discountedPrice") val discountedPrice: Int? = null,
    @SerialName("isSoldOut") val isSoldOut: Boolean
)
