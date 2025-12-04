package com.example.mireapractice.ui.components.currency_cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.mireapractice.R
import com.example.mireapractice.common.theme.Color1
import com.example.mireapractice.common.theme.Color2
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
            .border(
                width = 2.dp,
                color = Color1,
                shape = RoundedCornerShape(SIXTEEN.dp)
            )
            .background(
                Color(0xFFFFE5CC),
                RoundedCornerShape(SIXTEEN.dp)
            )
            .padding(SIXTEEN.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Верхняя часть: название валюты и разница
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Название валюты
                Text(
                    text = currencyItem.name,
                    fontSize = TWENTY.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color2,
                    modifier = Modifier.weight(1f)
                )
                
                // Стрелка и разница
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (currencyItem.isUp) {
                        ArrowUp(color = diffColor, size = 12.dp)
                    } else {
                        ArrowDown(color = diffColor, size = 12.dp)
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = "$diffSign ${formatNumber(Math.abs(currencyItem.diffValue))} ₽",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = diffColor
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = "${formatPercent(Math.abs(currencyItem.diffPercent))}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = diffColor
                    )
                }
            }
            
            Spacer(Modifier.height(SIXTEEN.dp))
            
            // Нижняя часть: флаги и значения валют
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TWELVE.dp)
            ) {
                // Левая секция: флаг валюты и код
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (currencyItem.flagUrl != null) {
                        AsyncImage(
                            model = currencyItem.flagUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .background(
                                Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${currencyItem.nominal} ${currencyItem.charCode}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
                
                // Знак равенства
                Text(
                    text = "=",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                // Правая секция: флаг рубля и значение
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Флаг России
                    Image(
                        painter = painterResource(id = R.drawable.russian_flag),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    
                    Box(
                        modifier = Modifier
                            .background(
                                Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${formatNumber(currencyItem.rubValue)} RUB",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
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
