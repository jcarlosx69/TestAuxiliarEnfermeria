package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.nuevaapptestauxiliarenfermeria.TestViewModel

@Composable
fun RandomTestScreen(testViewModel: TestViewModel, onExit: () -> Unit) {
    val randomQuestions = remember { testViewModel.questions.shuffled() }
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Char?>(null) }
    var answered by remember { mutableStateOf(false) }
    var showFinishDialog by remember { mutableStateOf(false) }

    if (currentIndex >= randomQuestions.size) {
        showFinishDialog = true
    }

    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Test aleatorio finalizado") },
            text = { Text("Has completado el test aleatorio. ¿Qué deseas hacer?") },
            confirmButton = {
                Button(onClick = { onExit() }) {
                    Text("Salir")
                }
            },
            dismissButton = {
                Button(onClick = {
                    currentIndex = 0
                    selectedAnswer = null
                    answered = false
                    showFinishDialog = false
                }) {
                    Text("Reiniciar")
                }
            }
        )
        return
    }

    // Mostrar el número original de la pregunta
    val displayNumber = randomQuestions[currentIndex].number
    val question = randomQuestions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Pregunta $displayNumber: ${question.text}",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnswerOption(
                optionLetter = 'A',
                optionText = question.optionA,
                isSelected = selectedAnswer == 'A',
                correctAnswer = question.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'A'; answered = true }
            )
            AnswerOption(
                optionLetter = 'B',
                optionText = question.optionB,
                isSelected = selectedAnswer == 'B',
                correctAnswer = question.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'B'; answered = true }
            )
            AnswerOption(
                optionLetter = 'C',
                optionText = question.optionC,
                isSelected = selectedAnswer == 'C',
                correctAnswer = question.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'C'; answered = true }
            )
            AnswerOption(
                optionLetter = 'D',
                optionText = question.optionD,
                isSelected = selectedAnswer == 'D',
                correctAnswer = question.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'D'; answered = true }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = displayNumber.toFloat() / randomQuestions.size,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (answered) {
                        currentIndex++
                        selectedAnswer = null
                        answered = false
                    }
                },
                enabled = answered,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Siguiente pregunta")
            }
            Button(
                onClick = { onExit() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Salir")
            }
        }
    }
}
