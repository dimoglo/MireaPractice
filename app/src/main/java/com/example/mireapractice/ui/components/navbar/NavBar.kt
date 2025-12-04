package com.example.mireapractice.ui.components.navbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .layoutId(NavBarDefaults.NavBarLeftIconLocator),
                contentAlignment = Alignment.Center
            ) {
                left(Modifier)
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .layoutId(NavBarDefaults.NavBarTitleLocator),
                contentAlignment = Alignment.Center
            ) {
                middle(Modifier)
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .layoutId(NavBarDefaults.NavBarRightIconLocator),
                contentAlignment = Alignment.Center
            ) {
                right(Modifier)
            }
        },
        modifier = modifier
            .height(NavBarHeight.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        measurePolicy = measurePolicy
    )
}