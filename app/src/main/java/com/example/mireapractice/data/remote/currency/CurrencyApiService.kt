package com.example.mireapractice.data.remote.currency

import retrofit2.http.GET

interface CurrencyApiService {
    @GET("daily_json.js")
    suspend fun getExchangeRate(): ExchangeRateDto
}