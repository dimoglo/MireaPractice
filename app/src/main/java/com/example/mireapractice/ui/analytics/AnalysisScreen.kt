package com.example.mireapractice.ui.analytics

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mireapractice.R
import com.example.mireapractice.common.theme.Color1
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.ui.components.bottombar.BottomBar
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.components.navbar.NavBar
import com.example.mireapractice.ui.components.navbar.NavBarLayoutMeasurePolicy
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel = hiltViewModel(),
    onTabSelected: (BottomBarType) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }
    var showCurrencyPicker by remember { mutableStateOf(false) }

    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    }
    val inputDateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    val fromDateLong = remember(uiState.fromDate) {
        if (uiState.fromDate.isNotEmpty()) {
            try {
                val dateOnly = if (uiState.fromDate.contains("T")) {
                    uiState.fromDate.substringBefore("T")
                } else {
                    uiState.fromDate
                }
                val localDate = java.time.LocalDate.parse(dateOnly)
                localDate.atStartOfDay(java.time.ZoneOffset.UTC).toInstant().toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        } else {
            System.currentTimeMillis()
        }
    }

    val toDateLong = remember(uiState.toDate) {
        if (uiState.toDate.isNotEmpty()) {
            try {
                val dateOnly = if (uiState.toDate.contains("T")) {
                    uiState.toDate.substringBefore("T")
                } else {
                    uiState.toDate
                }
                val localDate = java.time.LocalDate.parse(dateOnly)
                localDate.atStartOfDay(java.time.ZoneOffset.UTC).toInstant().toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        } else {
            System.currentTimeMillis()
        }
    }

    val fromDatePickerState = rememberDatePickerState(initialSelectedDateMillis = fromDateLong)
    val toDatePickerState = rememberDatePickerState(initialSelectedDateMillis = toDateLong)
    val currencyPickerSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                        text = "Анализ",
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

            // Контент
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = SIXTEEN.dp),
                contentPadding = PaddingValues(vertical = SIXTEEN.dp),
                verticalArrangement = Arrangement.spacedBy(SIXTEEN.dp)
            ) {
                // Выбор дат
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "C:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color1,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            // Дата "С"
                            DateSelectorField(
                                label = null,
                                date = uiState.fromDate,
                                onClick = { showFromDatePicker = true },
//                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.padding(end = SIXTEEN.dp))

                            Text(
                                text = "По:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color1,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            // Дата "По"
                            DateSelectorField(
                                label = null,
                                date = uiState.toDate,
                                onClick = { showToDatePicker = true },
//                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Метрики
                if (uiState.maxValue != null && uiState.minValue != null) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(SIXTEEN.dp)
                        ) {
                            MetricCard(
                                label = "Самый высокий курс за выбранный период",
                                value = formatValue(uiState.maxValue!!),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                label = "Самый низкий курс за выбранный период",
                                value = formatValue(uiState.minValue!!),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SIXTEEN.dp)
                    ) {
                        // График
                        Text(
                            text = "График",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color1,
                        )
                        // Выбор валюты
                        CurrencySelectorField(
                            currency = uiState.selectedCurrency,
                            onClick = { showCurrencyPicker = true },
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                Color.White.copy(alpha = 0.6f),
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                2.dp,
                                Color1,
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        if (uiState.dataPoints.isNotEmpty()) {
                            LineChart(
                                dataPoints = uiState.dataPoints,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(SIXTEEN.dp)
                            )
                        } else if (uiState.isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Загрузка...",
                                    fontSize = 16.sp,
                                    color = Color2
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = uiState.error ?: "Нет данных",
                                    fontSize = 16.sp,
                                    color = Color2
                                )
                            }
                        }
                    }
                }
            }

            // BottomBar
            BottomBar(
                selectedTab = BottomBarType.ANALYSIS,
                onTabSelected = onTabSelected
            )
        }

        // Диалоги выбора дат
        if (showFromDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showFromDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            fromDatePickerState.selectedDateMillis?.let {
                                viewModel.onFromDateSelected(it)
                            }
                            showFromDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showFromDatePicker = false }) {
                        Text("Отмена")
                    }
                }
            ) {
                DatePicker(state = fromDatePickerState)
            }
        }

        if (showToDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showToDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            toDatePickerState.selectedDateMillis?.let {
                                viewModel.onToDateSelected(it)
                            }
                            showToDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showToDatePicker = false }) {
                        Text("Отмена")
                    }
                }
            ) {
                DatePicker(state = toDatePickerState)
            }
        }

        // Диалог выбора валюты
        if (showCurrencyPicker) {
            ModalBottomSheet(
                onDismissRequest = { showCurrencyPicker = false },
                sheetState = currencyPickerSheetState,
                containerColor = Color.White
            ) {
                CurrencyPickerDialog(
                    currencies = uiState.currencies,
                    onCurrencySelected = { currency ->
                        viewModel.onCurrencySelected(currency)
                        showCurrencyPicker = false
                    },
                    onDismiss = { showCurrencyPicker = false }
                )
            }
        }
    }
}

@Composable
fun DateSelectorField(
    label: String?,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    }
    val inputDateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    Box(
        modifier = modifier
            .background(
                Color(0xFFE3F2FD),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                Color1,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = SIXTEEN.dp, vertical = 12.dp)
    ) {
        Column {
            if (label != null) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color2.copy(alpha = 0.7f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (date.isNotEmpty()) {
                        try {
                            val dateOnly = if (date.contains("T")) {
                                date.substringBefore("T")
                            } else {
                                date
                            }
                            val parsedDate = inputDateFormat.parse(dateOnly)
                            parsedDate?.let { dateFormat.format(it) } ?: date
                        } catch (e: Exception) {
                            date
                        }
                    } else {
                        ""
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "▼",
                    fontSize = 10.sp,
                    color = Color(0xFF1976D2)
                )
            }
        }
    }
}

@Composable
fun CurrencySelectorField(
    currency: com.example.mireapractice.domain.CurrencyModel?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Color(0xFFE3F2FD),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                Color1,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = SIXTEEN.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currency?.name ?: "Выберите валюту",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1976D2)
            )
            Text(
                text = "▼",
                fontSize = 10.sp,
                color = Color(0xFF1976D2)
            )
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Color.White.copy(alpha = 0.9f),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Color1,
                RoundedCornerShape(16.dp)
            )
            .padding(SIXTEEN.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color2.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .background(
                        Color1.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color2
                )
            }
        }
    }
}

@Composable
fun LineChart(
    dataPoints: List<CurrencyDataPoint>,
    modifier: Modifier = Modifier
) {
    if (dataPoints.isEmpty()) return

    val maxValue = dataPoints.maxOf { it.value.toDouble() }
    val minValue = dataPoints.minOf { it.value.toDouble() }
    val valueRange = maxValue - minValue

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40.dp.toPx()

        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2

        // Рисуем сетку
        val gridColor = Color1.copy(alpha = 0.2f)
        val gridStrokeWidth = 1.dp.toPx()

        // Горизонтальные линии сетки
        for (i in 0..4) {
            val y = padding + (chartHeight / 4) * i
            drawLine(
                color = gridColor,
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = gridStrokeWidth
            )
        }

        // Вертикальные линии сетки
        val stepX = chartWidth / (dataPoints.size - 1).coerceAtLeast(1)
        for (i in dataPoints.indices) {
            val x = padding + stepX * i
            drawLine(
                color = gridColor,
                start = Offset(x, padding),
                end = Offset(x, height - padding),
                strokeWidth = gridStrokeWidth
            )
        }

        // Рисуем линию графика
        if (dataPoints.size > 1) {
            val path = Path()
            val stepX = chartWidth / (dataPoints.size - 1).coerceAtLeast(1)

            dataPoints.forEachIndexed { index, point ->
                val x = padding + stepX * index
                val normalizedValue = if (valueRange > 0) {
                    (point.value.toDouble() - minValue) / valueRange
                } else {
                    0.5
                }
                val y = height - padding - (chartHeight * normalizedValue.toFloat())

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            // Рисуем линию
            drawPath(
                path = path,
                color = Color1,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Рисуем точки
            dataPoints.forEachIndexed { index, _ ->
                val x = padding + stepX * index
                val normalizedValue = if (valueRange > 0) {
                    (dataPoints[index].value.toDouble() - minValue) / valueRange
                } else {
                    0.5
                }
                val y = height - padding - (chartHeight * normalizedValue.toFloat())

                drawCircle(
                    color = Color1,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
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
            if (currency.flag != null && currency.flag != "RUB_FLAG") {
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

private fun formatValue(value: BigDecimal): String {
    return if (value >= BigDecimal.ONE) {
        value.setScale(2, java.math.RoundingMode.HALF_UP).toString()
    } else {
        value.setScale(4, java.math.RoundingMode.HALF_UP).toString()
    }
}
