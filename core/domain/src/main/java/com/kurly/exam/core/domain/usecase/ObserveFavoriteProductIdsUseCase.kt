package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteProductIdsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return favoriteRepository.getFavoriteIds()
    }
}