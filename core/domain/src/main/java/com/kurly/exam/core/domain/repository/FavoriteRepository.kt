package com.kurly.exam.core.domain.repository

import com.kurly.exam.core.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing favorite products.
 */
interface FavoriteRepository {
    /**
     * Observes the list of favorited products.
     */
    fun getFavoriteProducts(): Flow<List<Product>>

    /**
     * Observes the set of favorite product IDs.
     */
    fun getFavoriteIds(): Flow<Set<Int>>

    /**
     * Toggles the favorite status of a product.
     * @param product The product domain model to be added or removed.
     */
    suspend fun toggleFavorite(product: Product)
}
