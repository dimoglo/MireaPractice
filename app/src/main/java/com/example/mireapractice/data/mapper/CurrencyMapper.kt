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
fun CurrencyDto.toEntity(exchangeRateDate: String): CurrencyEntity? {
    // Пропускаем валюты без charCode и numCode
    val codeForId = charCode.takeIf { it.isNotEmpty() && it.isNotBlank() } 
        ?: numCode.takeIf { it.isNotEmpty() && it.isNotBlank() }
        ?: return null
    
    return CurrencyEntity().apply {
        id = "${codeForId}_$exchangeRateDate"
        exchangeRateId = exchangeRateDate
        numCode = this@toEntity.numCode
        charCode = this@toEntity.charCode
        nominal = this@toEntity.nominal
        name = this@toEntity.name
        // Сохраняем как String (Realm не поддерживает BigDecimal напрямую)
        value = BigDecimal.valueOf(this@toEntity.value).toString()
        previous = BigDecimal.valueOf(this@toEntity.previous).toString()
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
    // Конвертируем String обратно в BigDecimal
    val valueBigDecimal = value?.let { BigDecimal(it) } ?: BigDecimal.ZERO
    val previousBigDecimal = previous?.let { BigDecimal(it) } ?: BigDecimal.ZERO
    
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