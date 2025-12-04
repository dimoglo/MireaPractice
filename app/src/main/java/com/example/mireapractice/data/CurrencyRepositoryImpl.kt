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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            val old = query<ExchangeRateEntity>("date == $0", exchangeEntity.date).first().find()
            if (old != null) delete(old)
            copyToRealm(exchangeEntity)
        }

        // Обрабатываем список валют вместо Map
        dto.currencies.forEach { currencyDto ->
            val entity = currencyDto.toEntity(exchangeEntity.date)
            realm.write {
                val old = query<CurrencyEntity>("id == $0", entity.id).first().find()
                if (old != null) delete(old)
                copyToRealm(entity)
            }
        }
        return@withContext getCachedExchangeRate() ?: throw IllegalStateException()
    }

    override suspend fun getCachedExchangeRate(): ExchangeRateModel? = withContext(Dispatchers.IO) {
        val ex = realm.query<ExchangeRateEntity>().first().find() ?: return@withContext null

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
}