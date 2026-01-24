package com.kurly.exam.core.ui.model

import androidx.compose.runtime.Immutable

// UI 전용 모델 정의 (Recomposition 최적화를 위해 Immutable 선언)
@Immutable
data class ProductUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean
) {
    val discountRate: Int? by lazy(mode = LazyThreadSafetyMode.NONE) {
        if (discountedPrice != null && originalPrice > 0) {
            ((originalPrice - discountedPrice).toDouble() / originalPrice * 100).toInt()
        } else {
            null
        }
    }
}
