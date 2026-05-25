package com.neki.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.neki.app.R

val Pixelify = FontFamily(
    Font(R.font.pixelify_regular, FontWeight.Normal),
    Font(R.font.pixelify_medium, FontWeight.Medium),
    Font(R.font.pixelify_semibold, FontWeight.SemiBold),
    Font(R.font.pixelify_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Bold,
        fontSize = 96.sp
    ),

    displayMedium = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    labelSmall = TextStyle(
        fontFamily = Pixelify,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)