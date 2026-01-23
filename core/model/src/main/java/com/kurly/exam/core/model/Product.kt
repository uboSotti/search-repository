package com.kurly.exam.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean
)
