package com.example.mireapractice.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mireapractice.data.remote.news.NewsApiService
import com.example.mireapractice.domain.CurrencyRepository
import com.example.mireapractice.ui.components.banner.BannerUi
import com.example.mireapractice.ui.components.currency_cards.CurrencyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val newsApiService: NewsApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Загружаем курсы валют и новости параллельно
                val currenciesDeferred = async {
                    try {
                        val exchangeRate = currencyRepository.fetchAndCacheExchangeRate()
                        val currencies = exchangeRate.currencies
                        
                        val currencyItems = currencies.map { currency ->
                            // Пересчитываем стоимость за единицу валюты
                            val rubValuePerUnit = currency.value.toDouble() / currency.nominal
                            val diffValuePerUnit = (currency.difference?.toDouble() ?: 0.0) / currency.nominal
                            
                            CurrencyItem(
                                flagUrl = currency.flag,
                                name = currency.name,
                                nominal = 1, // Всегда показываем номинал как 1
                                charCode = currency.charCode,
                                rubValue = rubValuePerUnit,
                                diffValue = diffValuePerUnit,
                                diffPercent = currency.percentChange?.toDouble() ?: 0.0,
                                isUp = currency.isGrowing == true
                            )
                        }
                        
                        Pair(exchangeRate.date, currencyItems)
                    } catch (e: Exception) {
                        Timber.e(e, "Ошибка загрузки валют")
                        // Пытаемся загрузить из кэша
                        val cached = currencyRepository.getCachedExchangeRate()
                        if (cached != null) {
                            val currencyItems = cached.currencies.map { currency ->
                                // Пересчитываем стоимость за единицу валюты
                                val rubValuePerUnit = currency.value.toDouble() / currency.nominal
                                val diffValuePerUnit = (currency.difference?.toDouble() ?: 0.0) / currency.nominal
                                
                                CurrencyItem(
                                    flagUrl = currency.flag,
                                    name = currency.name,
                                    nominal = 1, // Всегда показываем номинал как 1
                                    charCode = currency.charCode,
                                    rubValue = rubValuePerUnit,
                                    diffValue = diffValuePerUnit,
                                    diffPercent = currency.percentChange?.toDouble() ?: 0.0,
                                    isUp = currency.isGrowing == true
                                )
                            }
                            Pair(cached.date, currencyItems)
                        } else {
                            throw e
                        }
                    }
                }
                
                val newsDeferred = async {
                    try {
                        loadNews()
                    } catch (e: Exception) {
                        Timber.e(e, "Ошибка загрузки новостей")
                        getMockNews()
                    }
                }
                
                val (selectedDate, currencyItems) = currenciesDeferred.await()
                val news = newsDeferred.await()
                
                _uiState.value = _uiState.value.copy(
                    currencies = currencyItems,
                    news = news,
                    selectedDate = selectedDate,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                Timber.e(e, "Критическая ошибка загрузки данных")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message,
                    news = getMockNews() // Показываем моки новостей даже при ошибке
                )
            }
        }
    }
    
    private suspend fun loadNews(): List<BannerUi> {
        return try {
            val response = newsApiService.getWaterEnergyData(
                format = "json",
                qterm = "currency exchange rate",
                displayTitle = "1",
                fl = "id,displayTitle,pdfUrl,url",
                rows = 5,
                os = 0
            )
            
            val newsList = mutableListOf<BannerUi>()
            response.documents?.values?.forEach { doc ->
                doc.displayTitle?.let { title ->
                    newsList.add(
                        BannerUi(
                            title = title,
                            subtitle = doc.url,
                            imageUrl = doc.pdfUrl
                        )
                    )
                }
            }
            
            if (newsList.isEmpty()) {
                getMockNews()
            } else {
                newsList
            }
        } catch (e: Exception) {
            Timber.e(e, "Ошибка загрузки новостей из API")
            getMockNews()
        }
    }
    
    private fun getMockNews(): List<BannerUi> {
        return listOf(
            BannerUi(
                title = "ЦБ РФ объявил о новых мерах поддержки валютного рынка",
                subtitle = "Центральный банк России принял решение о дополнительных мерах стабилизации курса рубля",
                imageUrl = null
            )
        )
    }

    fun onDateSelected(date: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateString = dateFormat.format(date)
                
                // Пытаемся загрузить данные для выбранной даты из кэша
                val exchangeRate = currencyRepository.getExchangeRateByDate(selectedDateString)
                
                if (exchangeRate != null) {
                    val currencyItems = exchangeRate.currencies.map { currency ->
                        val rubValuePerUnit = currency.value.toDouble() / currency.nominal
                        val diffValuePerUnit = (currency.difference?.toDouble() ?: 0.0) / currency.nominal
                        
                        CurrencyItem(
                            flagUrl = currency.flag,
                            name = currency.name,
                            nominal = 1,
                            charCode = currency.charCode,
                            rubValue = rubValuePerUnit,
                            diffValue = diffValuePerUnit,
                            diffPercent = currency.percentChange?.toDouble() ?: 0.0,
                            isUp = currency.isGrowing == true
                        )
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        currencies = currencyItems,
                        selectedDate = selectedDateString,
                        isLoading = false,
                        error = null
                    )
                } else {
                    // Если данных нет в кэше, загружаем свежие данные
                    loadData()
                }
            } catch (e: Exception) {
                Timber.e(e, "Ошибка загрузки валют для даты")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onNotificationClick() {
        // Обработка нажатия на колокольчик
    }
}

data class HomeUiState(
    val currencies: List<CurrencyItem> = emptyList(),
    val news: List<BannerUi> = emptyList(),
    val selectedDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

