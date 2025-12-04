package com.example.mireapractice.ui.components.bottombar

import com.example.mireapractice.R

enum class BottomBarType(val label: String, val iconResId: Int) {
    HOME("Главная", R.drawable.home),
    EXCHANGE("Обмен", R.drawable.exchange),
    CALCULATOR("Калькулятор", R.drawable.calculate),
    ANALYTICS("Анализ", R.drawable.analysis)
}