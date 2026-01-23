package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(productId: Int) {
        favoriteRepository.toggleFavorite(productId)
    }
}