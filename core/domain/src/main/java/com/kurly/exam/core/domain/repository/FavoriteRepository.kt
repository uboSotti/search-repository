package com.kurly.exam.core.domain.repository

import com.kurly.exam.core.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * 찜한 상품을 관리하는 리포지토리 인터페이스.
 */
interface FavoriteRepository {
    /**
     * 찜한 모든 상품의 목록을 관찰합니다.
     *
     * @return 찜한 [Product] 목록을 방출하는 [Flow].
     */
    fun getFavoriteProducts(): Flow<List<Product>>

    /**
     * 찜한 모든 상품의 ID 집합을 관찰합니다.
     *
     * @return 찜한 상품의 ID [Set]을 방출하는 [Flow].
     */
    fun getFavoriteIds(): Flow<Set<Int>>

    /**
     * 상품의 찜 상태를 토글합니다.
     * 찜 목록에 상품이 있으면 제거하고, 없으면 추가합니다.
     *
     * @param product 찜 상태를 변경할 [Product] 객체.
     */
    suspend fun toggleFavorite(product: Product)
}
