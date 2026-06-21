package com.timenw.drinktracker.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 饮酒统计 App 配色 — 琥珀/金色主题，契合酒文化
private val AmberPrimary = Color(0xFFFFB300)
private val AmberOnPrimary = Color(0xFF3E2723)
private val AmberContainer = Color(0xFFFFE082)
private val AmberOnContainer = Color(0xFF4E342E)

private val BrownSecondary = Color(0xFF8D6E63)
private val BrownOnSecondary = Color(0xFFFFFFFF)
private val BrownContainer = Color(0xFFD7CCC8)
private val BrownOnContainer = Color(0xFF3E2723)

private val RedTertiary = Color(0xFFE57373)
private val RedOnTertiary = Color(0xFFFFFFFF)
private val RedContainer = Color(0xFFFFCDD2)
private val RedOnContainer = Color(0xFFB71C1C)

// 健康提醒色
val AlcoholSafe = Color(0xFF4CAF50)
val AlcoholWarning = Color(0xFFFF9800)
val AlcoholDanger = Color(0xFFF44336)

private val DarkColorScheme = darkColorScheme(
    primary = AmberPrimary,
    onPrimary = AmberOnPrimary,
    primaryContainer = AmberContainer,
    onPrimaryContainer = AmberOnContainer,
    secondary = BrownSecondary,
    onSecondary = BrownOnSecondary,
    secondaryContainer = BrownContainer,
    onSecondaryContainer = BrownOnContainer,
    tertiary = RedTertiary,
    onTertiary = RedOnTertiary,
    tertiaryContainer = RedContainer,
    onTertiaryContainer = RedOnContainer,
    background = Color(0xFF1A120B),
    surface = Color(0xFF2C1E14),
    surfaceVariant = Color(0xFF3E2E20),
    onBackground = Color(0xFFF5E6CC),
    onSurface = Color(0xFFF5E6CC),
    onSurfaceVariant = Color(0xFFD7CCC8)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF8D6E63),
    onPrimary = Color.White,
    primaryContainer = AmberContainer,
    onPrimaryContainer = AmberOnContainer,
    secondary = BrownSecondary,
    onSecondary = BrownOnSecondary,
    secondaryContainer = BrownContainer,
    onSecondaryContainer = BrownOnContainer,
    tertiary = RedTertiary,
    onTertiary = RedOnTertiary,
    tertiaryContainer = RedContainer,
    onTertiaryContainer = RedOnContainer,
    background = Color(0xFFFFF8F0),
    surface = Color.White,
    surfaceVariant = Color(0xFFF5E6CC),
    onBackground = Color(0xFF3E2723),
    onSurface = Color(0xFF3E2723),
    onSurfaceVariant = Color(0xFF6D4C41)
)

@Composable
fun DrinkTrackerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
