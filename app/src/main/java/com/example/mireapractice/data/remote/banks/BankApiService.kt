package com.example.mireapractice.data.remote.banks

import retrofit2.http.Body
import retrofit2.http.POST

interface BankApiService {
    @POST("findById/bank")
    suspend fun findBankById(
        @Body body: BankQueryBody
    ): DaDataBankResponseDto
}

data class BankQueryBody(
    val query: String,
    val count: Int? = null
)