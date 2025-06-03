package com.l3on1kl.forkolsa.domain.repository

import com.l3on1kl.forkolsa.data.mapper.toDomain
import com.l3on1kl.forkolsa.data.remote.api.TrainingApi
import com.l3on1kl.forkolsa.domain.model.Training
import com.l3on1kl.forkolsa.domain.model.Video
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val api: TrainingApi
) : TrainingRepository {

    override suspend fun getTrainings(): List<Training> {
        return api.getTrainings().map { it.toDomain() }
    }

    override suspend fun getVideo(id: Int): Video {
        return api.getVideo(id).toDomain()
    }
}
