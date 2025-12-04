package com.example.mireapractice.ui.components.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mireapractice.common.theme.Color1
import com.example.mireapractice.common.theme.Color2
import com.example.mireapractice.common.utils.Constants.EIGHT
import com.example.mireapractice.common.utils.Constants.EIGHTY
import com.example.mireapractice.common.utils.Constants.FOURTEEN
import com.example.mireapractice.common.utils.Constants.SIX
import com.example.mireapractice.common.utils.Constants.SIXTEEN
import com.example.mireapractice.common.utils.Constants.TWELVE
import com.example.mireapractice.common.utils.Constants.TWENTY

@Composable
fun Banner(
    banner: BannerUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Transparent, RoundedCornerShape(TWENTY.dp))
            .border(
                width = 1.dp,
                color = Color1,
                shape = RoundedCornerShape(TWENTY.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(TWELVE.dp)
        ) {
            Text(
                text = banner.title,
                fontSize = SIXTEEN.sp,
                color = Color2.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )

            if (banner.subtitle != null) {
                Spacer(Modifier.height(SIX.dp))

                Text(
                    text = banner.subtitle,
                    fontSize = TWELVE.sp,
                    color = Color2.copy(alpha = 0.7f)
                )
            }
        }

        if (banner.imageUrl != null) {
            Spacer(Modifier.width(TWELVE.dp))

            AsyncImage(
                model = banner.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(EIGHTY.dp)
                    .clip(RoundedCornerShape(SIXTEEN.dp))
            )
        }
    }
}
