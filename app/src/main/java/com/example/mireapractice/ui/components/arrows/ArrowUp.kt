package com.example.mireapractice.ui.components.arrows

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ArrowUp(
    color: Color,
    size: Dp = 24.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()

        drawPath(
            path = Path().apply {
                moveTo(w / 2f, 0f)
                lineTo(0f, h)
                lineTo(w, h)
                close()
            },
            color = color
        )
    }
}
