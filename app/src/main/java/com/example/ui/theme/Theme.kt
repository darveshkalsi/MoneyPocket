package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = BrandPrimaryContainer,
    onPrimaryContainer = SurfaceLight,
    secondary = BrandSecondary,
    onSecondary = SurfaceLight,
    secondaryContainer = BrandSecondaryContainer,
    onSecondaryContainer = SurfaceLight,
    tertiary = SuccessDark,
    onTertiary = SurfaceLight,
    tertiaryContainer = SuccessEmeraldContainer,
    onTertiaryContainer = SuccessDark,
    background = BackgroundLight,
    onBackground = TextHeadlineSlate,
    surface = SurfaceLight,
    onSurface = TextHeadlineSlate,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = TextBodySlate,
    outline = BorderHairline,
    outlineVariant = BorderLight,
    error = ErrorRed,
    onError = SurfaceLight,
    errorContainer = ErrorRedContainer,
    onErrorContainer = ErrorRedDark
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryContainer,
    onPrimary = SurfaceLight,
    primaryContainer = BrandPrimaryDeep,
    onPrimaryContainer = SurfaceLight,
    secondary = BrandSecondaryContainer,
    onSecondary = SurfaceLight,
    tertiary = SuccessEmerald,
    onTertiary = TextHeadlineSlate,
    tertiaryContainer = SuccessDark,
    onTertiaryContainer = SuccessEmeraldContainer,
    background = TextHeadlineSlate,
    onBackground = BackgroundLight,
    surface = TextHeadlineSlate,
    onSurface = BackgroundLight,
    surfaceVariant = TextBodySlate,
    onSurfaceVariant = SurfaceContainerHigh,
    outline = TextSecondarySlate,
    outlineVariant = BorderLight,
    error = ErrorRed,
    onError = SurfaceLight
)

@Composable
fun PocketMoneyPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
