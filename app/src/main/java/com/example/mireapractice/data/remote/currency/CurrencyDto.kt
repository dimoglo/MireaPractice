package com.example.mireapractice.data.remote.currency

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * DTO для ответа API курсов валют ЦБ РФ
 * Структура: https://api-flag-5ti4.onrender.com/api/currencies
 */
data class ExchangeRateDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("currencies")
    val currencies: List<CurrencyDto>
)

/**
 * DTO для отдельной валюты из API ЦБ РФ
 */
data class CurrencyDto(
    @SerializedName("charCode")
    val charCode: String,
    @SerializedName("numCode")
    val numCode: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("nominal")
    val nominal: Int,
    @SerializedName("previous")
    val previous: Double,
    @SerializedName("flag")
    val flag: String? = null
)