package com.kurly.exam.jnsk.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 프로젝트 내 모든 Navigation Destination을 정의하는 sealed interface.
 * Navigation 3.0의 타입 세이프(type-safe) 내비게이션 및 상태 복원을 위해 사용된다.
 */
sealed interface Screen {
    /**
     * 메인 화면 (feature:main 모듈)
     */
    @Serializable
    data object Main : NavKey

    /**
     * 찜한 상품 화면 (feature:favorite 모듈)
     */
    @Serializable
    data object Favorite : NavKey

    // @Serializable
    // data class ProductDetail(val productId: Int) : NavKey
}
