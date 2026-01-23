package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 섹션 목록 및 페이징 정보를 포함하는 API 응답 데이터 전송 객체(DTO).
 *
 * @property data 섹션 목록([SectionDto])을 담고 있는 리스트.
 * @property paging 페이징 정보([PagingDto]). 다음 페이지가 없는 경우 null일 수 있습니다.
 */
@Serializable
data class SectionsResponse(
    @SerialName("data")
    val data: List<SectionDto>,
    @SerialName("paging")
    val paging: PagingDto? = null
)
