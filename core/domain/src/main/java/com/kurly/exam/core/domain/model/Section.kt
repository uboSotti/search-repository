package com.kurly.exam.core.domain.model

data class Section(
    val title: String,
    val id: Int,
    val type: String,
    val url: String
)

data class Product(
    val id: Int,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    val isSoldOut: Boolean
)
