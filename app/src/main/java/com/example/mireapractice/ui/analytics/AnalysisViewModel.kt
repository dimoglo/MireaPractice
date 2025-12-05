package com.example.mireapractice.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mireapractice.domain.CurrencyModel
import com.example.mireapractice.domain.CurrencyRepository
import com.example.mireapractice.domain.ExchangeRateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val exchangeRate = currencyRepository.getCachedExchangeRate()
                val currencies = exchangeRate?.currencies ?: emptyList()

                // Устанавливаем текущую дату как начальную и конечную
                val today = Calendar.getInstance()
                val yesterday = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -1)
                }

                val fromDateString = inputDateFormat.format(yesterday.time)
                val toDateString = inputDateFormat.format(today.time)

                _uiState.value = _uiState.value.copy(
                    currencies = currencies,
                    selectedCurrency = currencies.firstOrNull { it.charCode == "USD" } ?: currencies.firstOrNull(),
                    fromDate = fromDateString,
                    toDate = toDateString,
                    isLoading = false
                )

                loadAnalysisData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadAnalysisData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val fromDate = _uiState.value.fromDate
                val toDate = _uiState.value.toDate
                val selectedCurrency = _uiState.value.selectedCurrency

                if (fromDate.isEmpty() || toDate.isEmpty() || selectedCurrency == null) {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    return@launch
                }

                val exchangeRates = currencyRepository.getExchangeRatesByDateRange(fromDate, toDate)
                
                // Получаем данные по выбранной валюте за период
                val currencyData = exchangeRates.mapNotNull { exchangeRate ->
                    val currency = exchangeRate.currencies.firstOrNull { it.charCode == selectedCurrency.charCode }
                    if (currency != null) {
                        val dateOnly = if (exchangeRate.date.contains("T")) {
                            exchangeRate.date.substringBefore("T")
                        } else {
                            exchangeRate.date
                        }
                        CurrencyDataPoint(
                            date = dateOnly,
                            value = currency.value.divide(BigDecimal.valueOf(currency.nominal.toLong()), 10, java.math.RoundingMode.HALF_UP)
                        )
                    } else {
                        null
                    }
                }.sortedBy { it.date }

                if (currencyData.isNotEmpty()) {
                    val maxValue = currencyData.maxOf { it.value }
                    val minValue = currencyData.minOf { it.value }

                    _uiState.value = _uiState.value.copy(
                        dataPoints = currencyData,
                        maxValue = maxValue,
                        minValue = minValue,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        dataPoints = emptyList(),
                        isLoading = false,
                        error = "Нет данных за выбранный период"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onFromDateSelected(date: Long) {
        val dateString = inputDateFormat.format(date)
        _uiState.value = _uiState.value.copy(fromDate = dateString)
        loadAnalysisData()
    }

    fun onToDateSelected(date: Long) {
        val dateString = inputDateFormat.format(date)
        _uiState.value = _uiState.value.copy(toDate = dateString)
        loadAnalysisData()
    }

    fun onCurrencySelected(currency: CurrencyModel) {
        _uiState.value = _uiState.value.copy(selectedCurrency = currency)
        loadAnalysisData()
    }
}

data class AnalysisUiState(
    val currencies: List<CurrencyModel> = emptyList(),
    val selectedCurrency: CurrencyModel? = null,
    val fromDate: String = "",
    val toDate: String = "",
    val dataPoints: List<CurrencyDataPoint> = emptyList(),
    val maxValue: BigDecimal? = null,
    val minValue: BigDecimal? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

data class CurrencyDataPoint(
    val date: String,
    val value: BigDecimal
)
