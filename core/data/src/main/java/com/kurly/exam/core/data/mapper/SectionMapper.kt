package com.kurly.exam.core.data.mapper

import com.kurly.exam.core.data.remote.dto.ProductDto
import com.kurly.exam.core.data.remote.dto.SectionDto
import com.kurly.exam.core.domain.model.Product
import com.kurly.exam.core.domain.model.Section

fun SectionDto.toDomain(): Section {
    return Section(
        title = title,
        id = id,
        type = type,
        url = url
    )
}

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
