package com.l3on1kl.forkolsa.data.remote.dto

import com.squareup.moshi.Json

data class VideoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "duration") val duration: Int?,
    @Json(name = "link") val link: String?
)
