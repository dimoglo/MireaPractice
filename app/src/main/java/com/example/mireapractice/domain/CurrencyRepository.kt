package com.example.mireapractice.domain

interface CurrencyRepository {
    /**
     * Получить свежие курсы из API и занести в Realm
     */
    suspend fun fetchAndCacheExchangeRate(): ExchangeRateModel

    /**
     * Вернуть последний кэшированный ExchangeRateModel
     */
    suspend fun getCachedExchangeRate(): ExchangeRateModel?

    /**
     * Получить курсы валют по конкретной дате
     */
    suspend fun getExchangeRateByDate(date: String): ExchangeRateModel?

    /**
     * Вернуть список популярных валют
     */
    suspend fun getPopularCurrencies(): List<CurrencyModel>

    /**
     * Вернуть список всех валют
     */
    suspend fun getAllCurrencies(): List<CurrencyModel>
}
