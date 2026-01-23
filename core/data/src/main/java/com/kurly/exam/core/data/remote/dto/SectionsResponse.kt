package com.kurly.exam.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionsResponse(
    @SerialName("data")
    val data: List<SectionDto>,
    @SerialName("paging")
    val paging: PagingDto? = null
)