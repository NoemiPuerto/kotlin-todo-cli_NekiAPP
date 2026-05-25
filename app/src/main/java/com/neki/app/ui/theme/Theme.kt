package com.neki.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NekiColorScheme = lightColorScheme(
    primary = DkGreen,
    secondary = LgGreen,
    tertiary = AlGreen,

    background = BgColor,
    surface = BgBoxElements,
    surfaceVariant = BgClockElement,

    onPrimary = BgSelectedElement,
    onSecondary = BgSelectedElement,
    onTertiary = DarkFont,

    onBackground = DarkFont,
    onSurface = DarkFont
)

@Composable
fun NekiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NekiColorScheme,
        typography = Typography,
        content = content
    )
}