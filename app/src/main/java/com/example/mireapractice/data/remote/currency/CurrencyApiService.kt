package com.example.mireapractice.data.remote.currency

import retrofit2.http.GET

/**
 * API сервис для получения курсов валют ЦБ РФ
 * Base URL: https://api-flag-5ti4.onrender.com/
 */
interface CurrencyApiService {
    @GET("api/currencies")
    suspend fun getExchangeRate(): ExchangeRateDto
}