package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nuevaapptestauxiliarenfermeria.TestScreen
import com.example.nuevaapptestauxiliarenfermeria.TestViewModel

enum class Screen {
    Home, Test, Random, Review, Summary, Exit
}

@Composable
fun TestApp(testViewModel: TestViewModel = viewModel()) {
    var currentScreen = remember { mutableStateOf(Screen.Home) }
    var startIndex = remember { mutableStateOf(0) }
    val activity = LocalActivity.current

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFE0F7FA)
        ) {
            when (currentScreen.value) {
                Screen.Home -> HomeScreen(
                    onStartTest = { index ->
                        startIndex.value = index
                        testViewModel.setProgress(index)
                        currentScreen.value = Screen.Test
                    },
                    onReview = { currentScreen.value = Screen.Review },
                    onRandomMode = { currentScreen.value = Screen.Random }
                )
                Screen.Test -> TestScreen(testViewModel, onExit = { currentScreen.value = Screen.Summary })
                Screen.Random -> RandomTestScreen(testViewModel, onExit = { currentScreen.value =
                    Screen.Home
                })
                Screen.Review -> ReviewScreen(
                    testViewModel = testViewModel,
                    onFinish = { currentScreen.value = Screen.Home },
                    onExitReview = { currentScreen.value = Screen.Exit }
                )
                Screen.Summary -> {
                    SummaryScreen(testViewModel, onExit = {
                        activity.finishAffinity()
                        System.exit(0)
                    })
                }
                Screen.Exit -> ExitScreen(onExit = {
                    activity.finishAffinity()
                    System.exit(0)
                })
            }
        }
    }
}
