package com.example.mireapractice.domain

import java.math.BigDecimal

data class ExchangeRateModel (
    var date: String? = null,
    var previousDate: String? = null,
    var previousURL: String? = null,
    var timestamp: String? = null,
    var valutes: List<CurrencyModel>,
    var updateAt: Long? = null
)

data class CurrencyModel (
    var id: String? = null,
    var numCode: String? = null,
    var charCode: String? = null,
    var nominal: Int? = null,
    var name: String? = null,
    var value: BigDecimal? = null,
    var previous: BigDecimal? = null,
    val difference: BigDecimal? = null,
    var percentChange: BigDecimal? = null,
    val isGrowing: Boolean? = null
)