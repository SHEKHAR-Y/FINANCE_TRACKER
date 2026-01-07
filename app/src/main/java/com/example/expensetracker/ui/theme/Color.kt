package com.example.expensetracker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color.White,

    secondary = Color(0xFF475569),
    onSecondary = Color.White,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,

    error = Color(0xFFB00020),
    onError = Color.White,

    outline = Color.Gray
)
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color.White,

    secondary = Color(0xFF475569),
    onSecondary = Color.White,

    background = Color.Black,
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,

    error = Color(0xFFCF6679),
    onError = Color.Black,

    outline = Color(0xFFE5E7EB)
)