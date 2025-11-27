package com.example.mireapractice.data.local

import com.example.mireapractice.common.utils.Constants.EMPTY_STRING
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.math.BigDecimal

class ExchangeRateEntity: RealmObject {
    @PrimaryKey
    var date: String = EMPTY_STRING
    var previousDate: String? = null
    var previousURL: String? = null
    var timestamp: String? = null
    var updateAt: Long? = null
}

class CurrencyEntity: RealmObject {
    @PrimaryKey
    var id: String = EMPTY_STRING
    var exchangeRateId: String = EMPTY_STRING
    var numCode: String? = null
    var charCode: String? = null
    var nominal: Int? = null
    var name: String? = null
    var value: BigDecimal? = null
    var previous: BigDecimal? = null
}