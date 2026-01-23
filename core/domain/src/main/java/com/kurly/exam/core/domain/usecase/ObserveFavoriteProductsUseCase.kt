package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import com.kurly.exam.core.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes the list of favorited products by delegating directly to FavoriteRepository.
 */
class ObserveFavoriteProductsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return favoriteRepository.getFavoriteProducts()
    }
}
