package com.l3on1kl.forkolsa.data.mapper

import com.l3on1kl.forkolsa.data.remote.dto.TrainingDto
import com.l3on1kl.forkolsa.data.remote.dto.VideoDto
import com.l3on1kl.forkolsa.domain.model.Training
import com.l3on1kl.forkolsa.domain.model.Video

fun TrainingDto.toDomain() = Training(
    id,
    title ?: "",
    description ?: "",
    type ?: 0,
    duration?.toIntOrNull() ?: 0,
)

fun VideoDto.toDomain() = Video(
    id,
    duration ?: 0,
    link ?: ""
)
