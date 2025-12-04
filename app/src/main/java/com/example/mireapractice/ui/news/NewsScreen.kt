package com.example.mireapractice.ui.news

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import coil.compose.AsyncImage
import com.example.mireapractice.R
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.ui.components.bottombar.BottomBar
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.components.navbar.NavBar
import com.example.mireapractice.ui.components.navbar.NavBarLayoutMeasurePolicy
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.ui.components.banner.BannerUi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onTabSelected: (BottomBarType) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color2,
                        modifier = modifier
                            .size(28.dp)
                            .clickable { onBackClick() }
                    )
                },
                middle = { modifier ->
                    Text(
                        text = "Новости",
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

            // Контент - список новостей
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = SIXTEEN.dp, vertical = SIXTEEN.dp),
                verticalArrangement = Arrangement.spacedBy(SIXTEEN.dp)
            ) {
                items(uiState.news) { newsItem ->
                    Column {
                        Text(
                            text = newsItem.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color2,
                            modifier = Modifier.padding(bottom = SIXTEEN.dp)
                        )

                        if (newsItem.subtitle != null) {
                            Text(
                                text = newsItem.subtitle,
                                fontSize = 16.sp,
                                color = Color2.copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = SIXTEEN.dp)
                            )
                        }

                        if (newsItem.imageUrl != null) {
                            AsyncImage(
                                model = newsItem.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = SIXTEEN.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    
                    // Разделитель между новостями (кроме последней)
                    if (newsItem != uiState.news.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = SIXTEEN.dp),
                            thickness = 1.dp,
                            color = Color2.copy(alpha = 0.3f)
                        )
                    }
                }
            }

            // BottomBar
            BottomBar(
                selectedTab = BottomBarType.HOME,
                onTabSelected = onTabSelected
            )
        }
    }
}

