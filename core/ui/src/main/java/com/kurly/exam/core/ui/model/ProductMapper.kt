package com.kurly.exam.core.ui.model

import com.kurly.exam.core.model.Product

/**
 * [Product] 도메인 모델을 [ProductUiModel]로 변환합니다.
 *
 * @param isFavorite 해당 상품의 찜하기 여부.
 * @return [ProductUiModel]
 */
fun Product.toUiModel(isFavorite: Boolean): ProductUiModel {
    return ProductUiModel(
        id = id,
        name = name,
        imageUrl = image,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isSoldOut = isSoldOut,
        isFavorite = isFavorite
    )
}

/**
 * [ProductUiModel]을 [Product] 도메인 모델로 변환합니다.
 * UI 상태인 [ProductUiModel.isFavorite]는 도메인 모델에 영향을 주지 않습니다.
 */
fun ProductUiModel.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        image = imageUrl,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isSoldOut = isSoldOut
    )
}
