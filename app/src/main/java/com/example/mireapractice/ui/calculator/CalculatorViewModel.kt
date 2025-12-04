package com.example.mireapractice.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mireapractice.domain.CurrencyModel
import com.example.mireapractice.domain.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            try {
                val exchangeRate = currencyRepository.getCachedExchangeRate()
                val currencies = exchangeRate?.currencies ?: emptyList()
                
                // Добавляем RUB как базовую валюту
                // Используем специальный маркер для флага RUB
                val rubCurrency = CurrencyModel(
                    charCode = "RUB",
                    numCode = "643",
                    name = "Российский рубль",
                    value = BigDecimal.ONE,
                    nominal = 1,
                    previous = BigDecimal.ONE,
                    flag = "RUB_FLAG", // Специальный маркер для идентификации флага RUB
                    difference = null,
                    percentChange = null,
                    isGrowing = null
                )
                
                val allCurrencies = listOf(rubCurrency) + currencies
                
                _uiState.value = _uiState.value.copy(
                    currencies = allCurrencies,
                    fromCurrency = allCurrencies.firstOrNull { it.charCode == "USD" } ?: allCurrencies.firstOrNull(),
                    toCurrency = rubCurrency,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onInputChanged(input: String) {
        val cleanedInput = input.replace(",", ".").filter { it.isDigit() || it == '.' }
        
        if (cleanedInput.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                inputValue = "",
                outputValue = ""
            )
            return
        }

        try {
            val inputAmount = BigDecimal(cleanedInput)
            val fromCurrency = _uiState.value.fromCurrency
            val toCurrency = _uiState.value.toCurrency

            if (fromCurrency != null && toCurrency != null) {
                val convertedValue = convertCurrency(inputAmount, fromCurrency, toCurrency)
                val formattedOutput = formatNumber(convertedValue)

                _uiState.value = _uiState.value.copy(
                    inputValue = cleanedInput,
                    outputValue = formattedOutput
                )
            }
        } catch (e: Exception) {
            // Игнорируем ошибки парсинга
        }
    }

    fun onKeyPressed(key: String) {
        val currentInput = _uiState.value.inputValue
        
        when (key) {
            "backspace" -> {
                if (currentInput.isNotEmpty()) {
                    onInputChanged(currentInput.dropLast(1))
                }
            }
            "." -> {
                if (!currentInput.contains(".")) {
                    onInputChanged(currentInput + ".")
                }
            }
            else -> {
                onInputChanged(currentInput + key)
            }
        }
    }

    fun onFromCurrencySelected(currency: CurrencyModel) {
        val currentInput = _uiState.value.inputValue
        _uiState.value = _uiState.value.copy(fromCurrency = currency)
        
        // Пересчитываем результат при смене валюты
        if (currentInput.isNotEmpty()) {
            try {
                val inputAmount = BigDecimal(currentInput)
                val convertedValue = convertCurrency(inputAmount, currency, _uiState.value.toCurrency ?: return)
                val formattedOutput = formatNumber(convertedValue)
                _uiState.value = _uiState.value.copy(outputValue = formattedOutput)
            } catch (e: Exception) {
                // Игнорируем ошибки
            }
        }
    }

    fun onToCurrencySelected(currency: CurrencyModel) {
        val currentInput = _uiState.value.inputValue
        _uiState.value = _uiState.value.copy(toCurrency = currency)
        
        // Пересчитываем результат при смене валюты
        if (currentInput.isNotEmpty()) {
            try {
                val inputAmount = BigDecimal(currentInput)
                val convertedValue = convertCurrency(inputAmount, _uiState.value.fromCurrency ?: return, currency)
                val formattedOutput = formatNumber(convertedValue)
                _uiState.value = _uiState.value.copy(outputValue = formattedOutput)
            } catch (e: Exception) {
                // Игнорируем ошибки
            }
        }
    }

    fun showCurrencyPicker(isFromCurrency: Boolean) {
        _uiState.value = _uiState.value.copy(
            showCurrencyPicker = true,
            isSelectingFromCurrency = isFromCurrency
        )
    }

    fun hideCurrencyPicker() {
        _uiState.value = _uiState.value.copy(showCurrencyPicker = false)
    }

    fun selectCurrency(currency: CurrencyModel) {
        if (_uiState.value.isSelectingFromCurrency) {
            onFromCurrencySelected(currency)
        } else {
            onToCurrencySelected(currency)
        }
        hideCurrencyPicker()
    }

    private fun convertCurrency(amount: BigDecimal, from: CurrencyModel, to: CurrencyModel): BigDecimal {
        // Конвертируем через RUB
        // Если from = RUB, то просто умножаем на курс to
        // Если to = RUB, то делим на курс from
        // Иначе: amount * (from курс в RUB) / (to курс в RUB)
        
        val fromRateInRub = if (from.charCode == "RUB") {
            BigDecimal.ONE
        } else {
            from.value.divide(BigDecimal.valueOf(from.nominal.toLong()), 10, RoundingMode.HALF_UP)
        }
        
        val toRateInRub = if (to.charCode == "RUB") {
            BigDecimal.ONE
        } else {
            to.value.divide(BigDecimal.valueOf(to.nominal.toLong()), 10, RoundingMode.HALF_UP)
        }
        
        // Конвертируем: amount * (fromRateInRub / toRateInRub)
        return amount.multiply(fromRateInRub)
            .divide(toRateInRub, 10, RoundingMode.HALF_UP)
    }

    private fun formatNumber(value: BigDecimal): String {
        return if (value >= BigDecimal.ONE) {
            value.setScale(2, RoundingMode.HALF_UP).toString()
        } else {
            value.setScale(6, RoundingMode.HALF_UP).toString().trimEnd('0').trimEnd('.')
        }
    }
}

data class CalculatorUiState(
    val currencies: List<CurrencyModel> = emptyList(),
    val fromCurrency: CurrencyModel? = null,
    val toCurrency: CurrencyModel? = null,
    val inputValue: String = "",
    val outputValue: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val showCurrencyPicker: Boolean = false,
    val isSelectingFromCurrency: Boolean = true // true = выбираем from, false = выбираем to
)
