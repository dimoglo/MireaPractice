package com.example.mireapractice.ui.components.navbar

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import kotlin.math.max

internal class NavBarLayoutMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val leftPlaceable = measurables
            .find { it.layoutId == NavBarDefaults.NavBarLeftIconLocator }
            ?.measure(looseConstraints)
        val rightPlaceable = measurables
            .find { it.layoutId == NavBarDefaults.NavBarRightIconLocator }
            ?.measure(looseConstraints)
        val leftWidth = widthOrZero(leftPlaceable)
        val rightWidth = widthOrZero(rightPlaceable)
        val maxButtonsWidth = max(leftWidth, rightWidth)
        val middlePlaceable = measurables
            .find { it.layoutId == NavBarDefaults.NavBarTitleLocator }
            ?.measure(looseConstraints.offset(horizontal = -2 * maxButtonsWidth))
        val middleWidth = widthOrZero(middlePlaceable)
        val width = max(maxButtonsWidth * 2 + middleWidth, constraints.minWidth)
        val height = NavBarDefaults.NavBarHeight.dp.roundToPx()
        return layout(
            width = width,
            height = height
        ) {
            leftPlaceable?.placeRelative(
                x = 0,
                Alignment.CenterVertically.align(leftPlaceable.height, height)
            )
            rightPlaceable?.placeRelative(
                x = width - rightPlaceable.width,
                Alignment.CenterVertically.align(rightPlaceable.height, height)
            )
            val notOccupiedWidth = width - 2 * maxButtonsWidth

            middlePlaceable?.place(
                x = (notOccupiedWidth - middleWidth) / 2 + maxButtonsWidth,
                Alignment.CenterVertically.align(middlePlaceable.height, height)
            )
        }
    }
}

internal fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
internal fun heightOrZero(placeable: Placeable?) = placeable?.height ?: 0