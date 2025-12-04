package com.example.mireapractice.ui.components.currency_cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mireapractice.ui.components.arrows.ArrowDown
import com.example.mireapractice.ui.components.arrows.ArrowUp
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.common.utils.Constants.TWELVE
import com.example.mireapractice.common.utils.Constants.TWENTY
import java.text.DecimalFormat

@Composable
fun CurrencyCard(
    currencyItem: CurrencyItem,
    modifier: Modifier = Modifier
) {
    val diffColor = if (currencyItem.isUp) Color(0xFF4CAF50) else Color(0xFFF44336)
    val diffSign = if (currencyItem.isUp) "+" else "-"
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color(0xFFFFE5CC),
                RoundedCornerShape(SIXTEEN.dp)
            )
            .padding(SIXTEEN.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Левая часть: название валюты и разница
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = currencyItem.name,
                    fontSize = TWENTY.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(Modifier.height(TWELVE.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currencyItem.isUp) {
                        ArrowUp(color = diffColor, size = 16.dp)
                    } else {
                        ArrowDown(color = diffColor, size = 16.dp)
                    }
                    
                    Spacer(Modifier.width(8.dp))
                    
                    Text(
                        text = "$diffSign${formatNumber(currencyItem.diffValue)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = diffColor
                    )
                    
                    Spacer(Modifier.width(8.dp))
                    
                    Text(
                        text = "($diffSign${formatPercent(currencyItem.diffPercent)}%)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = diffColor
                    )
                }
            }
            
            // Правая часть: флаги и значения
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TWELVE.dp)
            ) {
                // Флаг и код валюты
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currencyItem.flagUrl != null) {
                        AsyncImage(
                            model = currencyItem.flagUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${currencyItem.nominal} ${currencyItem.charCode}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                
                Text(
                    text = "=",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                // Флаг рубля и значение
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Флаг России (можно использовать emoji или URL)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF0039A6), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🇷🇺",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = formatNumber(currencyItem.rubValue) + " RUB",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

private fun formatNumber(value: Double): String {
    val df = DecimalFormat("#.##")
    return df.format(value)
}

private fun formatPercent(value: Double): String {
    val df = DecimalFormat("#.##")
    return df.format(value)
}
