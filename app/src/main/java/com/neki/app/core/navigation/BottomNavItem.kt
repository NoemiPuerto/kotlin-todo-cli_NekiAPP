package com.neki.app.core.navigation

import androidx.annotation.DrawableRes

data class BottomNavItem(
    val route: String,
    val label: String,
    @param:DrawableRes val iconRes: Int
)