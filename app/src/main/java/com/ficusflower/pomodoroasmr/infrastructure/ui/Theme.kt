package com.ficusflower.pomodoroasmr.infrastructure.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ficusflower.pomodoroasmr.R

// Определяем цвета
private val PrimaryGreen = Color(0xFFAFBEA2)   // основной фон
private val TextDark = Color(0xFF51624F)       // цвет шрифта

private val TextDark2 = Color(0xFF232f21)       // цвет шрифта 2
private val AccentMint = Color(0xFF9CB4AC)     // акцентный (для кнопок, иконок)
private val AccentSand = Color(0xFFE6D5A9)     // дополнительный акцент (для выделений)

// Светлая цветовая схема (потом добавить тёмную(??))
private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,          // основной цвет темы
    secondary = AccentMint,         // второстепенный
    tertiary = AccentSand,          // третичный (для мелких акцентов)
    background = PrimaryGreen,      // фон экранов
    surface = PrimaryGreen,         // фон поверхностей (карточек, диалогов)
    onPrimary = TextDark,           // цвет текста на primary
    onSecondary = TextDark,         // цвет текста на secondary
    onBackground = TextDark,        // цвет текста на background
    onSurface = TextDark,           // цвет текста на surface
    surfaceVariant = TextDark2      //цвет текстовых меток
)

@Composable
fun PomodoroAppMainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // автоматически определяет тему системы
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,   // пока только светлая тема
        typography = typography(),         // стандартная типографика (можно расширить)
        content = content
    )
}

val KuraleFontFamily = FontFamily(Font(R.font.kurale_regular))

@Composable
fun typography() = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = KuraleFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = KuraleFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = TextDark2
    ),
    labelMedium = TextStyle(
        fontFamily = KuraleFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)
