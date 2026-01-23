package com.kurly.exam.core.domain.model

data class SectionWithProducts(
    val section: Section,
    val products: List<Product>
)
