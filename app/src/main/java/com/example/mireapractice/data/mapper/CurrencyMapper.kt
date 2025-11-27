package com.example.mireapractice.data.mapper

import com.example.mireapractice.data.local.CurrencyEntity
import com.example.mireapractice.data.local.ExchangeRateEntity
import com.example.mireapractice.data.remote.currency.CurrencyDto
import com.example.mireapractice.data.remote.currency.ExchangeRateDto
import com.example.mireapractice.domain.CurrencyModel
import com.example.mireapractice.domain.ExchangeRateModel

fun ExchangeRateDto.toEntity(): ExchangeRateEntity? {
    return ExchangeRateEntity().apply {
        date = this@toEntity.date
        previousDate = this@toEntity.previousDate
        previousURL = this@toEntity.previousUrl
        timestamp = this@toEntity.timestamp
        updateAt = System.currentTimeMillis()
    }
}

fun CurrencyDto.toEntity(exchangeRateDate: String): CurrencyEntity? {
    if(this.id == null)
        return null
    return CurrencyEntity().apply {
        id = this@toEntity.id
        exchangeRateId = exchangeRateDate
        numCode = this@toEntity.numCode
        charCode = this@toEntity.charCode
        nominal = this@toEntity.nominal
        name = this@toEntity.name
        value = this@toEntity.value
        previous = this@toEntity.previous
    }
}

fun ExchangeRateEntity.toModel(valutes: List<CurrencyModel>): ExchangeRateModel {
    return ExchangeRateModel(
        date = this.date,
        previousDate = this.previousDate,
        previousURL = this.previousURL,
        timestamp = this.timestamp,
        valutes = valutes,
        updateAt = this.updateAt
    )
}

fun CurrencyEntity.toModel(): CurrencyModel {
    val diff = if (value != null && previous != null) {
        value!!.subtract(previous!!)
    } else null

    return CurrencyModel(
        id = id,
        numCode = numCode,
        charCode = charCode,
        nominal = nominal,
        name = name,
        value = value,
        previous = previous,
        difference = diff,
        isGrowing = diff?.signum() == 1
    )
}