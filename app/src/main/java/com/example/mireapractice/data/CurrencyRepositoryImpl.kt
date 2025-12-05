package com.example.mireapractice.data

import com.example.mireapractice.common.utils.PopularCurrency
import com.example.mireapractice.data.local.CurrencyEntity
import com.example.mireapractice.data.local.ExchangeRateEntity
import com.example.mireapractice.data.mapper.toEntity
import com.example.mireapractice.data.mapper.toModel
import com.example.mireapractice.data.remote.currency.CurrencyApiService
import com.example.mireapractice.domain.CurrencyModel
import com.example.mireapractice.domain.CurrencyRepository
import com.example.mireapractice.domain.ExchangeRateModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApiService: CurrencyApiService,
    private val realm: Realm
) : CurrencyRepository {

    override suspend fun fetchAndCacheExchangeRate(): ExchangeRateModel = withContext(Dispatchers.IO) {
        val dto = currencyApiService.getExchangeRate()
        val exchangeEntity = dto.toEntity()

        realm.write {
            // Удаляем старые данные для этой даты
            val oldExchangeRate = query<ExchangeRateEntity>("date == $0", exchangeEntity.date).first().find()
            if (oldExchangeRate != null) {
                // Удаляем все валюты для этой даты
                val oldCurrencies = query<CurrencyEntity>("exchangeRateId == $0", oldExchangeRate.date).find()
                oldCurrencies.forEach { delete(it) }
                delete(oldExchangeRate)
            }
            
            // Удаляем все валюты, которые могут конфликтовать по ID
            // (на случай, если есть валюты с пустым charCode)
            val newCurrencyIds = dto.currencies.mapNotNull { currencyDto ->
                val codeForId = currencyDto.charCode.takeIf { it.isNotEmpty() && it.isNotBlank() } 
                    ?: currencyDto.numCode.takeIf { it.isNotEmpty() && it.isNotBlank() }
                codeForId?.let { "${it}_${exchangeEntity.date}" }
            }
            
            newCurrencyIds.forEach { newId ->
                val existing = query<CurrencyEntity>("id == $0", newId).first().find()
                if (existing != null) {
                    delete(existing)
                }
            }
            
            // Сохраняем новый ExchangeRateEntity
            copyToRealm(exchangeEntity)
            
            // Сохраняем все валюты (пропускаем только те, у которых нет ни charCode, ни numCode)
            dto.currencies.forEach { currencyDto ->
                val entity = currencyDto.toEntity(exchangeEntity.date)
                if (entity != null) {
                    copyToRealm(entity)
                }
            }
        }
        
        return@withContext getCachedExchangeRate() ?: throw IllegalStateException()
    }

    override suspend fun getCachedExchangeRate(): ExchangeRateModel? = withContext(Dispatchers.IO) {
        val ex = realm.query<ExchangeRateEntity>()
            .sort("updateAt", Sort.DESCENDING)
            .first()
            .find() ?: return@withContext null

        val currencies = realm
            .query<CurrencyEntity>("exchangeRateId == $0", ex.date)
            .find()
            .map { it.toModel() }

        return@withContext ex.toModel(currencies)
    }

    override suspend fun getExchangeRateByDate(date: String): ExchangeRateModel? = withContext(Dispatchers.IO) {
        val ex = realm.query<ExchangeRateEntity>("date == $0", date)
            .first()
            .find() ?: return@withContext null

        val currencies = realm
            .query<CurrencyEntity>("exchangeRateId == $0", ex.date)
            .find()
            .map { it.toModel() }

        return@withContext ex.toModel(currencies)
    }

    override suspend fun getPopularCurrencies(): List<CurrencyModel> = withContext(Dispatchers.IO) {
        val ex = realm.query<ExchangeRateEntity>().first().find() ?: return@withContext emptyList()

        val popularCodes = PopularCurrency.codes()

        val all = realm
            .query<CurrencyEntity>("exchangeRateId == $0", ex.date)
            .find()

        return@withContext all
            .filter { popularCodes.contains(it.charCode) }
            .map { it.toModel() }
    }

    override suspend fun getAllCurrencies(): List<CurrencyModel> = withContext(Dispatchers.IO) {
        val ex = realm.query<ExchangeRateEntity>().first().find() ?: return@withContext emptyList()

        return@withContext realm
            .query<CurrencyEntity>("exchangeRateId == $0", ex.date)
            .find()
            .map { it.toModel() }
    }

    override suspend fun getExchangeRatesByDateRange(fromDate: String, toDate: String): List<ExchangeRateModel> = withContext(Dispatchers.IO) {
        // Получаем все ExchangeRateEntity в диапазоне дат
        val exchangeRates = realm.query<ExchangeRateEntity>()
            .find()
            .filter { exchangeRate ->
                val dateOnly = if (exchangeRate.date.contains("T")) {
                    exchangeRate.date.substringBefore("T")
                } else {
                    exchangeRate.date
                }
                dateOnly >= fromDate && dateOnly <= toDate
            }
            .sortedBy { it.date }

        return@withContext exchangeRates.map { exchangeRate ->
            val currencies = realm
                .query<CurrencyEntity>("exchangeRateId == $0", exchangeRate.date)
                .find()
                .map { it.toModel() }
            exchangeRate.toModel(currencies)
        }
    }
}