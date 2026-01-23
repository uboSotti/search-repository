package com.kurly.exam.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Section(
    val title: String,
    val id: Int,
    val type: String,
    val url: String
)
