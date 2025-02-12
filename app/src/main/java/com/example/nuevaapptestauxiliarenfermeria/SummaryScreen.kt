package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun SummaryScreen(testViewModel: TestViewModel, onExit: () -> Unit) {
    val correctCount by testViewModel.correctCount.collectAsState()
    val wrongCount by testViewModel.wrongCount.collectAsState()
    val totalQuestions = testViewModel.questions.size

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Resumen de la sesión", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Total de preguntas: $totalQuestions", fontSize = 20.sp)
        Text(text = "Respuestas correctas: $correctCount", fontSize = 20.sp, color = Color(0xFFA5D6A7))
        Text(text = "Respuestas incorrectas: $wrongCount", fontSize = 20.sp, color = Color(0xFFEF9A9A))
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onExit() }) {
            Text("Salir")
        }
    }
}
