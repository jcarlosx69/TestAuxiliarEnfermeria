package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TestScreen(testViewModel: TestViewModel, onExit: () -> Unit) {
    var showExitDialog by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }

    val currentIndex by testViewModel.currentIndex.collectAsState()
    val question = testViewModel.questions.getOrNull(currentIndex)
    val selectedAnswer by testViewModel.selectedAnswer.collectAsState()
    val answered by testViewModel.answered.collectAsState()

    // Usamos siempre currentIndex + 1 para la numeración mostrada
    val displayNumber = currentIndex + 1

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
            text = "Pregunta $displayNumber: ${question?.text ?: "No hay más preguntas"}",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (question != null) {
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
                    onSelect = { testViewModel.selectAnswer('A') }
                )
                AnswerOption(
                    optionLetter = 'B',
                    optionText = question.optionB,
                    isSelected = selectedAnswer == 'B',
                    correctAnswer = question.correctAnswer,
                    answered = answered,
                    onSelect = { testViewModel.selectAnswer('B') }
                )
                AnswerOption(
                    optionLetter = 'C',
                    optionText = question.optionC,
                    isSelected = selectedAnswer == 'C',
                    correctAnswer = question.correctAnswer,
                    answered = answered,
                    onSelect = { testViewModel.selectAnswer('C') }
                )
                AnswerOption(
                    optionLetter = 'D',
                    optionText = question.optionD,
                    isSelected = selectedAnswer == 'D',
                    correctAnswer = question.correctAnswer,
                    answered = answered,
                    onSelect = { testViewModel.selectAnswer('D') }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = (currentIndex + 1).toFloat() / testViewModel.questions.size,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (currentIndex == testViewModel.questions.size - 1) {
                        showRestartDialog = true
                    } else {
                        testViewModel.nextQuestion()
                    }
                },
                enabled = answered,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Siguiente pregunta")
            }
            Button(
                onClick = { showExitDialog = true },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Salir")
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Confirmar salida") },
            text = { Text("¿Estás seguro de que deseas salir?") },
            confirmButton = {
                Button(onClick = {
                    showExitDialog = false
                    onExit()
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(onClick = { showExitDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    if (showRestartDialog) {
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text("Test completado") },
            text = { Text("Has contestado la última pregunta. ¿Deseas reiniciar el test desde el principio?") },
            confirmButton = {
                Button(onClick = {
                    showRestartDialog = false
                    testViewModel.resetTest()
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(onClick = { showRestartDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}
