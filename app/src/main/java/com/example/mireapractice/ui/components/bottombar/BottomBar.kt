package com.example.mireapractice.ui.components.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mireapractice.common.theme.Color1
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.common.utils.Constants.EIGHT
import com.example.mireapractice.common.utils.Constants.FOUR
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.common.utils.Constants.TEN
import com.example.mireapractice.common.utils.Constants.TWENTY
import com.example.mireapractice.common.utils.Constants.TWO

@Composable
fun BottomBar(
    selectedTab: BottomBarType,
    onTabSelected: (BottomBarType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(FOUR.dp, Color1, RoundedCornerShape(TWENTY.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomBarType.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = TEN.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = TWO.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = tab.iconResId),
                        contentDescription = tab.label,
                        colorFilter = ColorFilter.tint(if (isSelected) Color2 else Color1),
                        modifier = Modifier
                            .size(35.dp)
                    )
                    Text(
                        text = tab.label,
                        color = if (isSelected) Color2 else Color1,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}