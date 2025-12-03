package com.example.mireapractice.data.mapper

import com.example.mireapractice.common.utils.Constants.EMPTY_STRING
import com.example.mireapractice.data.local.CurrencyEntity
import com.example.mireapractice.data.local.ExchangeRateEntity
import com.example.mireapractice.data.remote.currency.CurrencyDto
import com.example.mireapractice.data.remote.currency.ExchangeRateDto
import com.example.mireapractice.domain.CurrencyModel
import com.example.mireapractice.domain.ExchangeRateModel
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Маппинг ExchangeRateDto в ExchangeRateEntity для сохранения в Realm
 */
fun ExchangeRateDto.toEntity(): ExchangeRateEntity {
    return ExchangeRateEntity().apply {
        date = this@toEntity.date
        timestamp = this@toEntity.timestamp
        updateAt = System.currentTimeMillis()
    }
}

/**
 * Маппинг CurrencyDto в CurrencyEntity для сохранения в Realm
 * Используется составной ключ: charCode + exchangeRateId
 */
fun CurrencyDto.toEntity(exchangeRateDate: String): CurrencyEntity {
    return CurrencyEntity().apply {
        // Составной ключ для уникальности валюты на конкретную дату
        id = "${charCode}_$exchangeRateDate"
        exchangeRateId = exchangeRateDate
        numCode = this@toEntity.numCode
        charCode = this@toEntity.charCode
        nominal = this@toEntity.nominal
        name = this@toEntity.name
        // Конвертируем Double в BigDecimal для точности вычислений
        value = BigDecimal.valueOf(this@toEntity.value)
        previous = BigDecimal.valueOf(this@toEntity.previous)
        flag = this@toEntity.flag
    }
}

/**
 * Маппинг ExchangeRateEntity в ExchangeRateModel для domain слоя
 */
fun ExchangeRateEntity.toModel(currencies: List<CurrencyModel>): ExchangeRateModel {
    return ExchangeRateModel(
        date = this.date,
        timestamp = this.timestamp ?: EMPTY_STRING,
        currencies = currencies,
        updateAt = this.updateAt
    )
}

/**
 * Маппинг CurrencyEntity в CurrencyModel для domain слоя
 * Вычисляет разницу, процент изменения и направление тренда
 */
fun CurrencyEntity.toModel(): CurrencyModel {
    val valueBigDecimal = value ?: BigDecimal.ZERO
    val previousBigDecimal = previous ?: BigDecimal.ZERO
    
    // Вычисляем разницу
    val diff = valueBigDecimal.subtract(previousBigDecimal)
    
    // Вычисляем процент изменения
    val percentChange = if (previousBigDecimal.compareTo(BigDecimal.ZERO) != 0) {
        diff.divide(previousBigDecimal, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal(100))
    } else null
    
    // Определяем направление тренда (растет или падает)
    val isGrowing = diff.signum() == 1

    return CurrencyModel(
        charCode = charCode,
        numCode = numCode,
        name = name,
        value = valueBigDecimal,
        nominal = nominal,
        previous = previousBigDecimal,
        flag = flag,
        difference = diff,
        percentChange = percentChange,
        isGrowing = isGrowing
    )
}