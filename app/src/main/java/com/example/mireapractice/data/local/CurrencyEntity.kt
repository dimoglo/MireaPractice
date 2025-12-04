package com.example.mireapractice.data.local

import com.example.mireapractice.common.utils.Constants.EMPTY_STRING
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ExchangeRateEntity: RealmObject {
    @PrimaryKey
    var date: String = EMPTY_STRING
    var timestamp: String? = null
    var updateAt: Long? = null
}

class CurrencyEntity: RealmObject {
    /**
     * Составной ключ: charCode + exchangeRateId для уникальности валюты на конкретную дату
     */
    @PrimaryKey
    var id: String = EMPTY_STRING
    var exchangeRateId: String = EMPTY_STRING
    var numCode: String = EMPTY_STRING
    var charCode: String = EMPTY_STRING
    var nominal: Int = 1
    var name: String = EMPTY_STRING
    // Используем String для хранения BigDecimal значений (Realm не поддерживает BigDecimal напрямую)
    var value: String? = null
    var previous: String? = null
    var flag: String? = null
}