package com.l3on1kl.forkolsa.data.remote.api

import com.l3on1kl.forkolsa.data.remote.dto.TrainingDto
import com.l3on1kl.forkolsa.data.remote.dto.VideoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainingApi {

    @GET("get_workouts")
    suspend fun getTrainings(): List<TrainingDto>

    @GET("get_video")
    suspend fun getVideo(
        @Query("id") id: Int
    ): VideoDto
}
