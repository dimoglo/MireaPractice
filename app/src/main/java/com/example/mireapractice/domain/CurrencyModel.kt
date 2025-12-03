package com.example.mireapractice.domain

import java.math.BigDecimal

/**
 * Domain модель для курсов валют ЦБ РФ
 */
data class ExchangeRateModel(
    val date: String,
    val timestamp: String,
    val currencies: List<CurrencyModel>,
    val updateAt: Long? = null
)

/**
 * Domain модель для отдельной валюты
 */
data class CurrencyModel(
    val charCode: String,
    val numCode: String,
    val name: String,
    val value: BigDecimal,
    val nominal: Int,
    val previous: BigDecimal,
    val flag: String? = null,
    val difference: BigDecimal? = null,
    val percentChange: BigDecimal? = null,
    val isGrowing: Boolean? = null
)