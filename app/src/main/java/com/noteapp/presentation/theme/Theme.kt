/*
 * Configuración del tema Material 3 utilizando el esquema de color oscuro personalizado.
 */
package com.noteapp.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = OnPrimary,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = Purple80,
    secondary = AccentSecondary,
    onSecondary = OnPrimary,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = PurpleGrey80,
    tertiary = AccentWarning,
    onTertiary = OnPrimary,
    background = DarkBackground,
    onBackground = OnBackground,
    surface = DarkSurface,
    onSurface = OnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnSurfaceSubtle,
    error = AccentError,
    onError = OnPrimary,
    outline = DividerColor,
    outlineVariant = DarkSurfaceVariant
)

@Composable
fun NoteAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}