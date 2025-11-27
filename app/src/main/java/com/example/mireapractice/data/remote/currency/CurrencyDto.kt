package com.example.mireapractice.data.remote.currency

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ExchangeRateDto(
    @SerializedName("Date")
    val date: String,
    @SerializedName("PreviousDate")
    val previousDate: String,
    @SerializedName("PreviousURL")
    val previousUrl: String,
    @SerializedName("Timestamp")
    val timestamp: String,
    @SerializedName("Valute")
    val valute: Map<String, CurrencyDto>
)

data class CurrencyDto(
    @SerializedName("ID")
    val id: String? = null,
    @SerializedName("NumCode")
    val numCode: String? = null,
    @SerializedName("CharCode")
    val charCode: String? = null,
    @SerializedName("Nominal")
    val nominal: Int? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Value")
    val value: BigDecimal? = null,
    @SerializedName("Previous")
    val previous: BigDecimal? = null
)