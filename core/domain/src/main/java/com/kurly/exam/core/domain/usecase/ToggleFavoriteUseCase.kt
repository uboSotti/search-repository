package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import com.kurly.exam.core.model.Product
import javax.inject.Inject

/**
 * 상품의 찜 상태를 토글하는 유즈케이스.
 *
 * @param favoriteRepository 찜하기 상태를 관리하는 리포지토리.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    /**
     * 유즈케이스를 실행하여 상품의 찜 상태를 변경합니다.
     *
     * @param product 찜 상태를 변경할 [Product] 객체.
     */
    suspend operator fun invoke(product: Product) {
        favoriteRepository.toggleFavorite(product)
    }
}
