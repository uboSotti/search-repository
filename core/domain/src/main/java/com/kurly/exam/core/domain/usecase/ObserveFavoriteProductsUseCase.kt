package com.kurly.exam.core.domain.usecase

import com.kurly.exam.core.domain.repository.FavoriteRepository
import com.kurly.exam.core.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 찜한 상품 목록의 변경사항을 관찰하는 유즈케이스.
 * [FavoriteRepository]에 직접 위임하여 찜한 상품 목록을 [Flow]로 제공받습니다.
 *
 * @param favoriteRepository 찜하기 상태를 관리하는 리포지토리.
 */
class ObserveFavoriteProductsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    /**
     * 유즈케이스를 실행하여 찜한 상품 목록의 [Flow]를 반환합니다.
     *
     * @return 찜한 [Product] 목록을 방출하는 [Flow].
     */
    operator fun invoke(): Flow<List<Product>> {
        return favoriteRepository.getFavoriteProducts()
    }
}
