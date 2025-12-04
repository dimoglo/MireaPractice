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
        
        /**
         * Возвращает порядковый номер валюты в списке популярных валют
         * Если валюта не популярная, возвращает Int.MAX_VALUE
         */
        fun getOrder(charCode: String): Int {
            return PopularCurrency.entries.indexOfFirst { it.code == charCode }
                .takeIf { it >= 0 } ?: Int.MAX_VALUE
        }
    }
}