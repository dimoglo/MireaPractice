package com.example.mireapractice.common.utils

enum class PopularCurrency(val code: String) {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    JPY("JPY"),
    CNY("CNY"),
    CHF("CHF"),
    TRY("TRY"),
    INR("INR");

    companion object {
        fun codes(): Set<String> = PopularCurrency.entries.map { it.code }.toSet()
    }
}