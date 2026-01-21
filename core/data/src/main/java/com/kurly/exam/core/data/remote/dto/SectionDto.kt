package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionsResponse(
    @SerialName("data")
    val data: List<SectionDto>,
    @SerialName("paging")
    val paging: PagingDto
)

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

@Serializable
data class PagingDto(
    @SerialName("next_page")
    val nextPage: Int?
)
