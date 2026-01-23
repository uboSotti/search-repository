package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    @SerialName("data") val data: List<ProductDto>
)
