package com.l3on1kl.forkolsa.data.remote.dto

import com.squareup.moshi.Json

data class TrainingDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "type") val type: Int?,
    @Json(name = "duration") val duration: String?
)
