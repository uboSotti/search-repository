package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API로부터 수신하는 섹션 데이터 전송 객체(DTO).
 *
 * @property title 섹션의 제목.
 * @property id 섹션의 고유 식별자.
 * @property type 섹션의 유형 (e.g., vertical, horizontal, grid).
 * @property url 섹션과 관련된 URL.
 */
@Serializable
data class SectionDto(
    @SerialName("title")
    val title: String,
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String
)

/**
 * 페이징 정보를 담는 데이터 전송 객체(DTO).
 *
 * @property nextPage 다음 페이지 번호. 마지막 페이지인 경우 null일 수 있습니다.
 */
@Serializable
data class PagingDto(
    @SerialName("next_page")
    val nextPage: Int?
)
