package com.example.futsim.ui.theme
import androidx.compose.ui.graphics.Color
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- TEMA ESCURO ATUALIZADO ---
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen, // Verde vibrante se destaca no escuro
    secondary = AccentColor,  // Amarelo para ações secundárias e destaques
    background = DarkThemeBackground,
    surface = DarkThemeSurface,
    onPrimary = LightThemeTextPrimary, // Texto sobre a cor primária (verde)
    onSecondary = LightThemeTextPrimary,
    onBackground = DarkThemeTextPrimary,
    onSurface = DarkThemeTextPrimary,
    error = Color(0xFFCF6679)
)

// Tema claro (mantido como opção)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = AccentColor,
    background = LightThemeBackground,
    surface = LightThemeSurface,
    onPrimary = Color.White,
    onSecondary = LightThemeTextPrimary,
    onBackground = LightThemeTextPrimary,
    onSurface = LightThemeTextPrimary,
    error = Color(0xFFB00020)
)

@Composable
fun FutSimTheme(
    // Força o tema escuro como padrão, ignorando a configuração do sistema
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb() // Barra de status com a cor da superfície
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}