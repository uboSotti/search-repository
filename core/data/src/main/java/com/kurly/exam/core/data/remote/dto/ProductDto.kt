package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionProductsResponse(
    @SerialName("data")
    val data: List<ProductDto>
)

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: String,
    @SerialName("originalPrice")
    val originalPrice: Int,
    @SerialName("discountedPrice")
    val discountedPrice: Int? = null,
    @SerialName("isSoldOut")
    val isSoldOut: Boolean
)
