package com.kurly.exam.core.data.mapper

import com.kurly.exam.core.data.remote.dto.ProductDto
import com.kurly.exam.core.data.remote.dto.SectionDto
import com.kurly.exam.core.model.Product
import com.kurly.exam.core.model.Section

/**
 * [SectionDto]를 도메인 모델인 [Section]으로 변환합니다.
 */
fun SectionDto.toDomain(): Section {
    return Section(
        title = title,
        id = id,
        type = type,
        url = url
    )
}

/**
 * [ProductDto]를 도메인 모델인 [Product]로 변환합니다.
 */
fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        image = image,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isSoldOut = isSoldOut
    )
}
