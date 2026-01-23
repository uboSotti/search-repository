package com.kurly.exam.core.domain.model

import com.kurly.exam.core.model.Product
import com.kurly.exam.core.model.Section

/**
 * 섹션([Section])과 해당 섹션에 포함된 상품 목록([Product])을 함께 나타내는 데이터 클래스.
 * 주로 UI 계층에서 섹션과 상품을 한 번에 표시하기 위해 사용됩니다.
 *
 * @property section 섹션 정보를 담고 있는 [Section] 객체.
 * @property products 해당 섹션에 속한 상품들의 리스트.
 */
data class SectionWithProducts(
    val section: Section,
    val products: List<Product>
)
