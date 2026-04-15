package com.example.pomodoroasmr.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Определяем цвета
private val PrimaryGreen = Color(0xFFAFBEA2)   // основной фон
private val TextDark = Color(0xFF51624F)       // цвет шрифта
private val AccentMint = Color(0xFF9CB4AC)     // акцентный (для кнопок, иконок)
private val AccentSand = Color(0xFFE6D5A9)     // дополнительный акцент (для выделений)

// Светлая цветовая схема (можно потом добавить тёмную)
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
)

@Composable
fun PomodoroAppMainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // автоматически определяет тему системы
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,   // пока только светлая тема
        typography = Typography(),         // стандартная типографика (можно расширить)
        content = content
    )
}

// Опционально: кастомная типографика с твоими шрифтами
@Composable
fun Typography() = MaterialTheme.typography