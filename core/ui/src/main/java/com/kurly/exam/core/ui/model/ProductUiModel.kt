package com.kurly.exam.core.ui.model

import androidx.compose.runtime.Immutable

/**
 * UI 전용 상품 데이터 모델.
 * [Immutable] 어노테이션을 통해 Recomposition 최적화를 돕습니다.
 *
 * @property id 상품 ID.
 * @property name 상품명.
 * @property imageUrl 상품 이미지 URL.
 * @property originalPrice 원래 가격.
 * @property discountedPrice 할인가. Nullable.
 * @property isSoldOut 품절 여부.
 * @property isFavorite 찜하기 여부.
 */
@Immutable
data class ProductUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean,
    val isFavorite: Boolean
) {
    /**
     * 할인율을 계산하여 반환합니다. 할인가가 없으면 null을 반환합니다.
     * 계산 비용을 줄이기 위해 lazy 초기화를 사용합니다.
     */
    val discountRate: Int? by lazy(mode = LazyThreadSafetyMode.NONE) {
        if (discountedPrice != null && originalPrice > 0) {
            ((originalPrice - discountedPrice).toDouble() / originalPrice * 100).toInt()
        } else {
            null
        }
    }
}
