package com.example.mireapractice.ui.calculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mireapractice.R
import com.example.mireapractice.common.theme.Color1
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.ui.components.bottombar.BottomBar
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.components.navbar.NavBar
import com.example.mireapractice.ui.components.navbar.NavBarLayoutMeasurePolicy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel(),
    onTabSelected: (BottomBarType) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
        ) {
            // Лого
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SIXTEEN.dp, vertical = SIXTEEN.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Валютный компас",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color2
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            // NavBar
            NavBar(
                left = { modifier ->
                    Box(modifier = modifier)
                },
                middle = { modifier ->
                    Text(
                        text = "Калькулятор",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color2,
                        modifier = modifier
                    )
                },
                right = { modifier ->
                    Box(modifier = modifier)
                },
                measurePolicy = NavBarLayoutMeasurePolicy(),
                modifier = Modifier.fillMaxWidth()
            )

            // Поля ввода/вывода
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SIXTEEN.dp, vertical = 64.dp),
                verticalArrangement = Arrangement.spacedBy(SIXTEEN.dp)
            ) {
                // Первое поле (ввод)
                CurrencyInputField(
                    value = uiState.inputValue,
                    currency = uiState.fromCurrency,
                    onCurrencyClick = {
                        viewModel.showCurrencyPicker(isFromCurrency = true)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Второе поле (вывод)
                CurrencyOutputField(
                    value = uiState.outputValue,
                    currency = uiState.toCurrency,
                    onCurrencyClick = {
                        viewModel.showCurrencyPicker(isFromCurrency = false)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Клавиатура
            CalculatorKeyboard(
                onKeyPressed = { key ->
                    viewModel.onKeyPressed(key)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // BottomBar
            BottomBar(
                selectedTab = BottomBarType.CALCULATOR,
                onTabSelected = onTabSelected
            )
        }

        // Диалог выбора валюты
        if (uiState.showCurrencyPicker) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideCurrencyPicker() },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                CurrencyPickerDialog(
                    currencies = uiState.currencies,
                    onCurrencySelected = { currency ->
                        viewModel.selectCurrency(currency)
                    },
                    onDismiss = { viewModel.hideCurrencyPicker() }
                )
            }
        }
    }
}

@Composable
fun CurrencyInputField(
    value: String,
    currency: com.example.mireapractice.domain.CurrencyModel?,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Color(0xFFFFE5CC),
                RoundedCornerShape(16.dp)
            )
            .border(
                2.dp,
                Color1,
                RoundedCornerShape(16.dp)
            )
            .padding(SIXTEEN.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.ifEmpty { "0" },
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color2,
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier
                    .background(
                        Color.White,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        Color1,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onCurrencyClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Флаг валюты
                if (currency != null) {
                    if (currency.flag == "RUB_FLAG") {
                        Image(
                            painter = painterResource(id = R.drawable.russian_flag),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else if (currency.flag != null) {
                        AsyncImage(
                            model = currency.flag,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(
                    text = currency?.charCode ?: "USD",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color2
                )
            }
        }
    }
}

@Composable
fun CurrencyOutputField(
    value: String,
    currency: com.example.mireapractice.domain.CurrencyModel?,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Color(0xFFFFE5CC),
                RoundedCornerShape(16.dp)
            )
            .border(
                2.dp,
                Color1,
                RoundedCornerShape(16.dp)
            )
            .padding(SIXTEEN.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.ifEmpty { "0" },
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color2,
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier
                    .background(
                        Color.White,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        Color1,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onCurrencyClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Флаг валюты
                if (currency != null) {
                    if (currency.flag == "RUB_FLAG") {
                        Image(
                            painter = painterResource(id = R.drawable.russian_flag),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else if (currency.flag != null) {
                        AsyncImage(
                            model = currency.flag,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(
                    text = currency?.charCode ?: "RUB",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color2
                )
            }
        }
    }
}

@Composable
fun CalculatorKeyboard(
    onKeyPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                Color.White.copy(alpha = 0.9f),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(SIXTEEN.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ряд 1: 1, 2, 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalculatorKey("1", onKeyPressed, Modifier.weight(1f))
            CalculatorKey("2", onKeyPressed, Modifier.weight(1f))
            CalculatorKey("3", onKeyPressed, Modifier.weight(1f))
        }

        // Ряд 2: 4, 5, 6
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalculatorKey("4", onKeyPressed, Modifier.weight(1f))
            CalculatorKey("5", onKeyPressed, Modifier.weight(1f))
            CalculatorKey("6", onKeyPressed, Modifier.weight(1f))
        }

        // Ряд 3: 7, 8, 9
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalculatorKey("7", onKeyPressed, Modifier.weight(1f))
            CalculatorKey("8", onKeyPressed, Modifier.weight(1f))
            CalculatorKey("9", onKeyPressed, Modifier.weight(1f))
        }

        // Ряд 4: backspace, 0, .
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalculatorKey("⌫", onKeyPressed, Modifier.weight(1f), isBackspace = true)
            CalculatorKey("0", onKeyPressed, Modifier.weight(1f))
            CalculatorKey(".", onKeyPressed, Modifier.weight(1f))
        }
    }
}

@Composable
fun CalculatorKey(
    text: String,
    onKeyPressed: (String) -> Unit,
    modifier: Modifier = Modifier,
    isBackspace: Boolean = false
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .background(
                Color1.copy(alpha = 0.2f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Color1,
                RoundedCornerShape(12.dp)
            )
            .clickable {
                if (isBackspace) {
                    onKeyPressed("backspace")
                } else {
                    onKeyPressed(text)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (isBackspace) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Удалить",
                tint = Color2,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CurrencyPickerDialog(
    currencies: List<com.example.mireapractice.domain.CurrencyModel>,
    onCurrencySelected: (com.example.mireapractice.domain.CurrencyModel) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SIXTEEN.dp)
    ) {
        Text(
            text = "Выберите валюту",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color2,
            modifier = Modifier.padding(bottom = SIXTEEN.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(currencies) { currency ->
                CurrencyPickerItem(
                    currency = currency,
                    onClick = {
                        onCurrencySelected(currency)
                    }
                )
            }
        }

        TextButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SIXTEEN.dp)
        ) {
            Text(
                text = "Отмена",
                fontSize = 16.sp,
                color = Color2
            )
        }
    }
}

@Composable
fun CurrencyPickerItem(
    currency: com.example.mireapractice.domain.CurrencyModel,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Color1,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(SIXTEEN.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Флаг валюты
            if (currency.flag == "RUB_FLAG") {
                Image(
                    painter = painterResource(id = R.drawable.russian_flag),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Crop
                )
            } else if (currency.flag != null) {
                AsyncImage(
                    model = currency.flag,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = currency.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color2
                )
                Text(
                    text = currency.charCode,
                    fontSize = 14.sp,
                    color = Color2.copy(alpha = 0.7f)
                )
            }
        }
    }
}
