package com.kurly.exam.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing favorite products.
 */
interface FavoriteRepository {
    /**
     * Observes the set of favorite product IDs.
     */
    fun getFavoriteIds(): Flow<Set<Int>>

    /**
     * Toggles the favorite status of a product.
     * @param productId The ID of the product.
     */
    suspend fun toggleFavorite(productId: Int)
}