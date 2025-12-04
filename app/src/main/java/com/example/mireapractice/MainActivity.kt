package com.example.mireapractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mireapractice.common.theme.MireaPracticeTheme
import com.example.mireapractice.ui.banks.ExchangeInBanksScreen
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.home.HomeScreen
import com.example.mireapractice.ui.news.NewsScreen
import com.example.mireapractice.ui.notifications.NotificationsScreen
import dagger.hilt.android.AndroidEntryPoint

enum class Screen {
    HOME,
    NOTIFICATIONS,
    NEWS,
    EXCHANGE_IN_BANKS
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MireaPracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    key(currentScreen) {
        when (currentScreen) {
            Screen.HOME -> {
                HomeScreen(
                    onNotificationClick = {
                        currentScreen = Screen.NOTIFICATIONS
                    },
                    onNewsClick = {
                        currentScreen = Screen.NEWS
                    },
                    onTabSelected = { tab ->
                        when (tab) {
                            BottomBarType.EXCHANGE -> {
                                currentScreen = Screen.EXCHANGE_IN_BANKS
                            }
                            else -> {
                                // Можно добавить логику для других табов
                            }
                        }
                    }
                )
            }
            Screen.NOTIFICATIONS -> {
                NotificationsScreen(
                    onBackClick = {
                        currentScreen = Screen.HOME
                    },
                    onTabSelected = { tab ->
                        // При выборе таба возвращаемся на главную
                        if (tab == BottomBarType.HOME) {
                            currentScreen = Screen.HOME
                        }
                    }
                )
            }
            Screen.NEWS -> {
                NewsScreen(
                    onBackClick = {
                        currentScreen = Screen.HOME
                    },
                    onTabSelected = { tab ->
                        // При выборе таба возвращаемся на главную
                        if (tab == BottomBarType.HOME) {
                            currentScreen = Screen.HOME
                        }
                    }
                )
            }
            Screen.EXCHANGE_IN_BANKS -> {
                ExchangeInBanksScreen(
                    onTabSelected = { tab ->
                        when (tab) {
                            BottomBarType.HOME -> {
                                currentScreen = Screen.HOME
                            }
                            else -> {
                                // Можно добавить логику для других табов
                            }
                        }
                    }
                )
            }
        }
    }
}