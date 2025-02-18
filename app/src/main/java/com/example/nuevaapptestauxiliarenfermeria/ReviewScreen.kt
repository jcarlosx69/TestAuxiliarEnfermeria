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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewScreen(
    testViewModel: TestViewModel,
    onFinish: () -> Unit,      // Callback para volver al menú principal (HomeScreen)
    onExitReview: () -> Unit   // Callback para salir del modo repaso (p. ej., ir a ExitScreen)
) {
    // Obtenemos la lista de estadísticas de preguntas con fallos > 0
    val reviewStats = testViewModel.getReviewQuestions()
    // Extraemos la lista de preguntas ordenadas (por mayor cantidad de fallos)
    val reviewQuestions = reviewStats.sortedByDescending { it.wrong }.map { it.question }

    if (reviewQuestions.isEmpty()) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("No hay repaso") },
            text = { Text("No se han registrado fallos en el test. No hay preguntas para repasar.") },
            confirmButton = {
                Button(onClick = { onFinish() }) {
                    Text("Volver a Inicio")
                }
            }
        )
        return
    }

    var currentReviewIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Char?>(null) }
    var answered by remember { mutableStateOf(false) }
    var showFinishDialog by remember { mutableStateOf(false) }

    // Verificamos si se han repasado todas las preguntas
    if (currentReviewIndex >= reviewQuestions.size) {
        showFinishDialog = true
    }

    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Repaso finalizado") },
            text = { Text("Has completado el repaso de las preguntas más falladas. ¿Qué deseas hacer?") },
            confirmButton = {
                Button(onClick = {
                    // Volver a repasar: reiniciamos el índice y los estados
                    currentReviewIndex = 0
                    selectedAnswer = null
                    answered = false
                    showFinishDialog = false
                }) {
                    Text("Volver a repasar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showFinishDialog = false
                    onExitReview()   // Salir del modo repaso
                }) {
                    Text("Salir")
                }
            }
        )
        return
    }

    // Se muestra el número original de la pregunta (proveniente del campo 'number')
    val displayNumber = reviewQuestions[currentReviewIndex].number
    val currentQuestion = reviewQuestions[currentReviewIndex]

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
            text = "Pregunta $displayNumber: ${currentQuestion.text}",
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
                optionText = currentQuestion.optionA,
                isSelected = selectedAnswer == 'A',
                correctAnswer = currentQuestion.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'A'; answered = true }
            )
            AnswerOption(
                optionLetter = 'B',
                optionText = currentQuestion.optionB,
                isSelected = selectedAnswer == 'B',
                correctAnswer = currentQuestion.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'B'; answered = true }
            )
            AnswerOption(
                optionLetter = 'C',
                optionText = currentQuestion.optionC,
                isSelected = selectedAnswer == 'C',
                correctAnswer = currentQuestion.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'C'; answered = true }
            )
            AnswerOption(
                optionLetter = 'D',
                optionText = currentQuestion.optionD,
                isSelected = selectedAnswer == 'D',
                correctAnswer = currentQuestion.correctAnswer,
                answered = answered,
                onSelect = { selectedAnswer = 'D'; answered = true }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = (currentReviewIndex + 1).toFloat() / reviewQuestions.size,
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
                        currentReviewIndex++
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
                onClick = { onExitReview() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Salir")
            }
        }
    }
}
