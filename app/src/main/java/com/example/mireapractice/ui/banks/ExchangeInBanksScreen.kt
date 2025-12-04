package com.example.mireapractice.ui.banks

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mireapractice.R
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.ui.components.bottombar.BottomBar
import com.example.mireapractice.ui.components.bottombar.BottomBarType
import com.example.mireapractice.ui.components.navbar.NavBar
import com.example.mireapractice.ui.components.navbar.NavBarLayoutMeasurePolicy

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeInBanksScreen(
    onTabSelected: (BottomBarType) -> Unit = {}
) {
    val context = LocalContext.current
    val webViewUrl = remember {
        "https://www.banki.ru/products/currency/?source=main_exchange_rates_converter"
    }

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

            // WebView
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        webViewClient = object : WebViewClient() {
                            override fun onReceivedSslError(
                                view: WebView?,
                                handler: SslErrorHandler?,
                                error: SslError?
                            ) {
                                // Разрешаем загрузку страницы даже при SSL ошибках
                                handler?.proceed()
                            }

                            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                // Инжектируем CSS сразу при начале загрузки страницы
                                val earlyHideScript = """
                                    (function() {
                                        // Создаем стиль для скрытия элементов банки.ру
                                        if (!document.getElementById('hide-banki-ru-style')) {
                                            var style = document.createElement('style');
                                            style.id = 'hide-banki-ru-style';
                                            style.innerHTML = `
                                                /* Скрываем только верхний хедер с логотипом банки.ру */
                                                header:first-of-type,
                                                [class*="site-header"],
                                                [id*="site-header"],
                                                [class*="page-header"],
                                                [id*="page-header"],
                                                /* Скрываем логотип банки.ру в хедере */
                                                header [class*="logo"],
                                                header [id*="logo"],
                                                header [class*="banki"],
                                                header [id*="banki"],
                                                /* Скрываем кнопку "Войти" и меню в хедере */
                                                header [class*="login"],
                                                header [class*="menu"],
                                                header [class*="burger"],
                                                header [class*="auth"],
                                                /* Скрываем навигацию в хедере */
                                                header nav,
                                                header [class*="nav"],
                                                /* Скрываем элементы с текстом "банки.ру" только в хедере */
                                                header a[href*="banki.ru"]:not([href*="products"]),
                                                header [class*="Banki"],
                                                header [class*="Logo"],
                                                /* Скрываем первый хедер на странице */
                                                body > header:first-child,
                                                body > div:first-child > header:first-child
                                                {
                                                    display: none !important;
                                                    visibility: hidden !important;
                                                    opacity: 0 !important;
                                                    height: 0 !important;
                                                    overflow: hidden !important;
                                                    margin: 0 !important;
                                                    padding: 0 !important;
                                                }
                                            `;
                                            if (document.head) {
                                                document.head.appendChild(style);
                                            } else {
                                                document.addEventListener('DOMContentLoaded', function() {
                                                    document.head.appendChild(style);
                                                });
                                            }
                                        }
                                    })();
                                """.trimIndent()
                                view?.evaluateJavascript(earlyHideScript, null)
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                // Инжектируем CSS и JavaScript для скрытия элементов банки.ру и отслеживания динамических изменений
                                val hideBankiRuScript = """
                                    (function() {
                                        // Убеждаемся, что стиль применен
                                        if (!document.getElementById('hide-banki-ru-style')) {
                                            var style = document.createElement('style');
                                            style.id = 'hide-banki-ru-style';
                                            style.innerHTML = `
                                                header:first-of-type,
                                                [class*="site-header"],
                                                [id*="site-header"],
                                                [class*="page-header"],
                                                [id*="page-header"],
                                                header [class*="logo"],
                                                header [id*="logo"],
                                                header [class*="banki"],
                                                header [id*="banki"],
                                                header [class*="login"],
                                                header [class*="menu"],
                                                header [class*="burger"],
                                                header [class*="auth"],
                                                header nav,
                                                header [class*="nav"],
                                                header a[href*="banki.ru"]:not([href*="products"]),
                                                header [class*="Banki"],
                                                header [class*="Logo"],
                                                body > header:first-child,
                                                body > div:first-child > header:first-child
                                                {
                                                    display: none !important;
                                                    visibility: hidden !important;
                                                    opacity: 0 !important;
                                                    height: 0 !important;
                                                    overflow: hidden !important;
                                                    margin: 0 !important;
                                                    padding: 0 !important;
                                                }
                                            `;
                                            document.head.appendChild(style);
                                        }
                                        
                                        // Функция для скрытия элементов банки.ру
                                        function hideBankiRuElements() {
                                            try {
                                                // Скрываем хедеры
                                                var headers = document.querySelectorAll('header');
                                                headers.forEach(function(header) {
                                                    var headerText = header.textContent || header.innerText || '';
                                                    if (headerText.includes('банки.ру') || 
                                                        headerText.includes('БАНКИ.РУ') || 
                                                        headerText.includes('Banki.ru')) {
                                                        var hasContent = header.querySelector('[class*="product"]') || 
                                                                         header.querySelector('[class*="currency"]') ||
                                                                         header.querySelector('[class*="rate"]');
                                                        if (!hasContent) {
                                                            header.style.display = 'none';
                                                        }
                                                    }
                                                });
                                                
                                                // Скрываем элементы с логотипом
                                                var logoElements = document.querySelectorAll('header [class*="logo"], header [id*="logo"]');
                                                logoElements.forEach(function(el) {
                                                    var parent = el.closest('header');
                                                    if (parent && !parent.querySelector('[class*="product"]')) {
                                                        el.style.display = 'none';
                                                    }
                                                });
                                            } catch(e) {
                                                console.log('Error hiding elements: ' + e);
                                            }
                                        }
                                        
                                        // Выполняем скрытие сразу
                                        hideBankiRuElements();
                                        
                                        // Используем MutationObserver для отслеживания динамически добавляемых элементов
                                        var observer = new MutationObserver(function(mutations) {
                                            hideBankiRuElements();
                                        });
                                        
                                        // Начинаем наблюдение за изменениями в DOM
                                        if (document.body) {
                                            observer.observe(document.body, {
                                                childList: true,
                                                subtree: true,
                                                attributes: true,
                                                attributeFilter: ['class', 'id']
                                            });
                                        } else {
                                            document.addEventListener('DOMContentLoaded', function() {
                                                observer.observe(document.body, {
                                                    childList: true,
                                                    subtree: true,
                                                    attributes: true,
                                                    attributeFilter: ['class', 'id']
                                                });
                                            });
                                        }
                                    })();
                                """.trimIndent()
                                view?.evaluateJavascript(hideBankiRuScript, null)
                            }
                        }
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        loadUrl(webViewUrl)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // BottomBar
            BottomBar(
                selectedTab = BottomBarType.EXCHANGE,
                onTabSelected = onTabSelected
            )
        }
    }
}