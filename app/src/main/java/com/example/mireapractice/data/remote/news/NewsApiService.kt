package com.example.mireapractice.data.remote.news

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("api/v3/wds")
    suspend fun getWaterEnergyData(
        @Query("format") format: String = "json",
        @Query("qterm") qterm: String,
        @Query("display_title") displayTitle: String,
        @Query("fl") fl: String,
        @Query("rows") rows: Int,
        @Query("os") os: Int
    ): NewsResponseDto
}