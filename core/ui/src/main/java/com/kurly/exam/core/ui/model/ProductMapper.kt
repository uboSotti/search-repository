package com.kurly.exam.core.ui.model

import com.kurly.exam.core.model.Product

/**
 * [Product] 도메인 모델을 [ProductUiModel]로 변환합니다.
 */
fun Product.toUiModel(): ProductUiModel {
    return ProductUiModel(
        id = id,
        name = name,
        imageUrl = image,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isSoldOut = isSoldOut
    )
}

/**
 * [ProductUiModel]을 [Product] 도메인 모델로 변환합니다.
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
