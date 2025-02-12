package com.example.nuevaapptestauxiliarenfermeria

import android.app.Activity
import androidx.compose.runtime.staticCompositionLocalOf

// Definimos un CompositionLocal para proveer la Activity actual
val LocalActivity = staticCompositionLocalOf<Activity> {
    error("No Activity provided")
}
