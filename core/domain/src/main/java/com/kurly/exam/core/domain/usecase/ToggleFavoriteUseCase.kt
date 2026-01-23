package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import com.kurly.exam.core.model.Product
import javax.inject.Inject

/**
 * Toggles the favorite status of a product.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    /**
     * @param product The product to toggle its favorite status.
     */
    suspend operator fun invoke(product: Product) {
        favoriteRepository.toggleFavorite(product)
    }
}
