package com.example.mireapractice.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mireapractice.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mireapractice.common.theme.Color1
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.ui.components.banner.Banner
import com.example.mireapractice.ui.components.bottombar.BottomBar
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.components.currency_cards.CurrencyCard
import com.example.mireapractice.ui.components.navbar.NavBar
import com.example.mireapractice.ui.components.navbar.NavBarDefaults
import com.example.mireapractice.ui.components.navbar.NavBarLayoutMeasurePolicy
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.common.utils.Constants.TWELVE
import com.example.mireapractice.common.utils.Constants.TWENTY_FOUR
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNotificationClick: () -> Unit = {},
    onTabSelected: (BottomBarType) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    }
    val inputDateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    val selectedDateLong = remember(uiState.selectedDate) {
        if (uiState.selectedDate.isNotEmpty()) {
            try {
                inputDateFormat.parse(uiState.selectedDate)?.time ?: System.currentTimeMillis()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        } else {
            System.currentTimeMillis()
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateLong
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomBar(
                selectedTab = BottomBarType.HOME,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
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

            // Верхняя часть с логотипом и NavBar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
            ) {
                // Row с "Валютный компас" и логотипом
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
                            text = "Главная",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color2,
                            modifier = modifier
                        )
                    },
                    right = { modifier ->
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Уведомления",
                            tint = Color2,
                            modifier = modifier
                                .size(28.dp)
                                .clickable { onNotificationClick() }
                        )
                    },
                    measurePolicy = NavBarLayoutMeasurePolicy()
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    horizontal = SIXTEEN.dp,
                    vertical = TWENTY_FOUR.dp + NavBarDefaults.NavBarHeight.dp + SIXTEEN.dp + 24.dp + SIXTEEN.dp // Отступ сверху для логотипа + NavBar
                ),
                verticalArrangement = Arrangement.spacedBy(SIXTEEN.dp)
            ) {
                // Новости
                item {
                    Text(
                        text = "Новости",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color1
                    )
                }

                items(uiState.news) { newsItem ->
                    Banner(banner = newsItem)
                }

                // Заголовок курсов валют
                item {
                    Spacer(Modifier.height(TWELVE.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Курсы ЦБ на",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color1
                        )

                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFE3F2FD),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { showDatePicker = true }
                                .padding(horizontal = SIXTEEN.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (uiState.selectedDate.isNotEmpty()) {
                                        try {
                                            val date = inputDateFormat.parse(uiState.selectedDate)
                                            date?.let { dateFormat.format(it) } ?: ""
                                        } catch (e: Exception) {
                                            uiState.selectedDate
                                        }
                                    } else {
                                        dateFormat.format(Calendar.getInstance().time)
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1976D2)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "▼",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1976D2)
                                )
                            }
                        }
                    }
                }

                // Список валют
                items(
                    items = uiState.currencies,
                    key = { it.charCode }
                ) { currency ->
                    CurrencyCard(currencyItem = currency)
                }

                if (uiState.currencies.isEmpty() && !uiState.isLoading) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Курсы валют не загружены",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            if (uiState.error != null) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Ошибка: ${uiState.error}",
                                    fontSize = 14.sp,
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Загрузка...",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onDateSelected(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

