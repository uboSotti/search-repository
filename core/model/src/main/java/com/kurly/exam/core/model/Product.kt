package com.kurly.exam.core.model

import kotlinx.serialization.Serializable

/**
 * 상품 정보를 나타내는 데이터 클래스.
 * 이 클래스는 앱의 여러 계층에서 사용되는 핵심 도메인 모델입니다.
 *
 * @property id 상품의 고유 식별자.
 * @property name 상품의 이름.
 * @property image 상품 이미지의 URL.
 * @property originalPrice 상품의 원래 가격.
 * @property discountedPrice 할인된 가격. 할인이 없는 경우 null일 수 있습니다.
 * @property isSoldOut 상품의 품절 여부.
 */
@Serializable
data class Product(
    val id: Int,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean
)
