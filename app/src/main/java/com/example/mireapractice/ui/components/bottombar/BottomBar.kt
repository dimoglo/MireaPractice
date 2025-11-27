package com.example.mireapractice.ui.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mireapractice.common.utils.Constants.FOUR
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.common.utils.Constants.TWO

@Composable
fun BottomBar(
    selectedTab: BottomBarType,
    onTabSelected: (BottomBarType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .border(FOUR.dp, Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomBarType.entries.forEach { tab ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(TWO.dp, Color.White)
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = SIXTEEN.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    color = if (tab == selectedTab) Color(0xFF4C6FFF) else Color.White, // синий
                    fontSize = SIXTEEN.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}