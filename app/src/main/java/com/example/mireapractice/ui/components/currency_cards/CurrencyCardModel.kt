package com.example.mireapractice.ui.components.currency_cards

data class CurrencyItem(
    val flagUrl: String?,     // URL флага из API
    val name: String,         // "Австралийский доллар"
    val nominal: Int,         // 1
    val charCode: String,     // "AUD"
    val rubValue: Double,     // 50.96
    val diffValue: Double,    // -0.03
    val diffPercent: Double,  // -0.06
    val isUp: Boolean         // true - рост, false - падение
)