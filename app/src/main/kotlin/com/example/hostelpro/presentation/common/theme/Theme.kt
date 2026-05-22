package com.example.hostelpro.presentation.common.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = PrimaryLightBlue,
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = SecondaryTeal,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = SecondaryLightTeal,
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = TertiaryOrange,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = TertiaryLightOrange,
    onTertiaryContainer = Color(0xFF6D4C41),
    error = ErrorRed,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDADA),
    onErrorContainer = Color(0xFF410002),
    background = LightBackground,
    onBackground = Color(0xFF1C1B1F),
    surface = LightSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = NeutralLightGray,
    onSurfaceVariant = NeutralGray,
    outline = LightOutline,
    outlineVariant = Color(0xFFCAC7D0),
    scrim = Color(0xFF000000),
    inverseSurface = NeutralDarkGray,
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = PrimaryLightBlue,
    surfaceDim = Color(0xFFDED8E0),
    surfaceBright = Color(0xFFFFFBFE),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F2FA),
    surfaceContainer = Color(0xFFF3EDF7),
    surfaceContainerHigh = Color(0xFFEDE7F1),
    surfaceContainerHighest = Color(0xFFE8E1EB)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLightBlue,
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = PrimaryDarkBlue,
    onPrimaryContainer = PrimaryLightBlue,
    secondary = SecondaryLightTeal,
    onSecondary = Color(0xFF004D40),
    secondaryContainer = SecondaryDarkTeal,
    onSecondaryContainer = SecondaryLightTeal,
    tertiary = TertiaryLightOrange,
    onTertiary = Color(0xFF6D4C41),
    tertiaryContainer = TertiaryDarkOrange,
    onTertiaryContainer = TertiaryLightOrange,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDADA),
    background = DarkBackground,
    onBackground = Color(0xFFE6E1E6),
    surface = DarkSurface,
    onSurface = Color(0xFFE6E1E6),
    surfaceVariant = NeutralDarkGray,
    onSurfaceVariant = Color(0xFFCAC7D0),
    outline = DarkOutline,
    outlineVariant = Color(0xFF4F4753),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE6E1E6),
    inverseOnSurface = Color(0xFF1C1B1F),
    inversePrimary = PrimaryBlue,
    surfaceDim = Color(0xFF121212),
    surfaceBright = Color(0xFF39383D),
    surfaceContainerLowest = Color(0xFF0D0D12),
    surfaceContainerLow = Color(0xFF1E1B20),
    surfaceContainer = Color(0xFF252428),
    surfaceContainerHigh = Color(0xFF2F2E33),
    surfaceContainerHighest = Color(0xFF3A383E)
)

@Composable
fun HostelProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
