package com.kurly.exam.core.model

import kotlinx.serialization.Serializable

/**
 * 섹션 정보를 나타내는 데이터 클래스.
 * 이 클래스는 앱의 여러 계층에서 사용되는 핵심 도메인 모델입니다.
 *
 * @property title 섹션의 제목.
 * @property id 섹션의 고유 식별자.
 * @property type 섹션의 유형 (e.g., "vertical", "horizontal", "grid").
 * @property url 섹션과 관련된 URL.
 */
@Serializable
data class Section(
    val title: String,
    val id: Int,
    val type: String,
    val url: String
)
