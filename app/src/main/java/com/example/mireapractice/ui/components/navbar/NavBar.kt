package com.example.mireapractice.ui.components.navbar

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.example.mireapractice.ui.components.navbar.NavBarDefaults.NavBarHeight

@Composable
internal inline fun NavBar(
    left: @Composable (Modifier) -> Unit,
    middle: @Composable (Modifier) -> Unit,
    right: @Composable (Modifier) -> Unit,
    measurePolicy: NavBarLayoutMeasurePolicy,
    modifier: Modifier = Modifier
) {
    Layout(
        content = {
            left(
                Modifier
                    .fillMaxHeight()
            )
            middle(
                Modifier
                    .fillMaxHeight()
            )
            right(
                Modifier
                    .fillMaxHeight()
            )
        },
        modifier = modifier
            .height(NavBarHeight.dp)
            .fillMaxWidth(),
        measurePolicy = measurePolicy
    )
}