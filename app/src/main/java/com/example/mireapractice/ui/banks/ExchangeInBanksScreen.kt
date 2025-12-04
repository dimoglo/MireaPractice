package com.example.mireapractice.ui.banks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mireapractice.R
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.ui.components.bottombar.BottomBar
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.components.navbar.NavBar
import com.example.mireapractice.ui.components.navbar.NavBarLayoutMeasurePolicy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeInBanksScreen(
    onTabSelected: (BottomBarType) -> Unit = {}
) {
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomBar(
                selectedTab = BottomBarType.CURRENCY_CHANGE,
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
            ) {
                // 1. Лого
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
                            text = "Обмен валют",
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
                Spacer(modifier = Modifier.padding(end = 16.dp))


            }
        }
    }
}