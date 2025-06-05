package com.example.futsim.ui.viewmodel

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFutSimViewModel = staticCompositionLocalOf<FutSimViewModel> {
    error("ViewModel não foi provido via CompositionLocalProvider")
}