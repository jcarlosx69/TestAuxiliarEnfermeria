package com.example.nuevaapptestauxiliarenfermeria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Proveemos la Activity actual usando LocalActivity.
            CompositionLocalProvider(LocalActivity provides this) {
                TestApp()
            }
        }
    }
}