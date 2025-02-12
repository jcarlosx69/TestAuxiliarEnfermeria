package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column

@Composable
fun SelectStartButton(onSelect: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) {
        Text("Seleccionar desde...")
    }
    if (showDialog) {
        var input by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Selecciona punto de inicio") },
            text = {
                Column {
                    Text("Ingresa el número de pregunta para iniciar:")
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        label = { Text("Número de Pregunta") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val index = input.toIntOrNull() ?: 0
                    onSelect(index)
                    showDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
