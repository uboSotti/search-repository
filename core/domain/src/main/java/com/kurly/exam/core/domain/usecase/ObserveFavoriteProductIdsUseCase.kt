package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 찜한 상품의 ID 목록 변경사항을 관찰하는 유즈케이스.
 *
 * @param favoriteRepository 찜하기 상태를 관리하는 리포지토리.
 */
class ObserveFavoriteProductIdsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    /**
     * 유즈케이스를 실행하여 찜한 상품 ID 목록의 [Flow]를 반환합니다.
     *
     * @return 찜한 상품의 ID [Set]을 방출하는 [Flow].
     */
    operator fun invoke(): Flow<Set<Int>> {
        return favoriteRepository.getFavoriteIds()
    }
}
