package com.l3on1kl.forkolsa.domain.repository

import com.l3on1kl.forkolsa.domain.model.Training
import com.l3on1kl.forkolsa.domain.model.Video

interface TrainingRepository {

    suspend fun getTrainings(): List<Training>

    suspend fun getVideo(id: Int): Video

}
