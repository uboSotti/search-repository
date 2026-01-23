package com.kurly.exam.core.domain.model

import com.kurly.exam.core.model.Product
import com.kurly.exam.core.model.Section

/**
 * Paging Source에서 사용되는 도메인 모델.
 * 특정 섹션과 그에 포함된 상품 목록을 나타냅니다.
 */
data class SectionWithProducts(
    val section: Section,
    val products: List<Product>
)
