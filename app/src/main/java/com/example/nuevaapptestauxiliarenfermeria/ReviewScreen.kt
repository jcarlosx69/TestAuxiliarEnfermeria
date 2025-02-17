package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewScreen(
    testViewModel: TestViewModel,
    onFinish: () -> Unit,      // Callback para volver al menú (HomeScreen)
    onExitReview: () -> Unit   // Callback para salir (por ejemplo, ir a ExitScreen)
) {
    // Obtenemos la lista de estadísticas de preguntas con fallos > 0
    val reviewStats = testViewModel.getReviewQuestions()
    // Extraemos la lista de preguntas ordenadas de mayor a menor fallos
    val reviewQuestions = reviewStats.sortedByDescending { it.wrong }.map { it.question }

    // Si no hay preguntas para repasar, mostramos un diálogo y retornamos
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

    // Estado para llevar la cuenta del índice de repaso
    var currentReviewIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Char?>(null) }
    var answered by remember { mutableStateOf(false) }

    // Si hemos llegado al final de la lista, mostramos el diálogo de fin de repaso.
    if (currentReviewIndex >= reviewQuestions.size) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Repaso finalizado") },
            text = { Text("Has completado el repaso de las preguntas más falladas. ¿Qué deseas hacer?") },
            confirmButton = {
                Button(onClick = { onExitReview() }) {
                    Text("Salir")
                }
            },
            dismissButton = {
                Button(onClick = { onFinish() }) {
                    Text("Volver al menú")
                }
            }
        )
        return
    }

    // Usamos un número secuencial basado en el índice para mostrar la numeración en pantalla.
    val reviewDisplayNumber = currentReviewIndex + 1
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
            text = "Pregunta $reviewDisplayNumber: ${currentQuestion.text}",
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
            progress = reviewDisplayNumber.toFloat() / reviewQuestions.size,
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
