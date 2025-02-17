package com.example.nuevaapptestauxiliarenfermeria

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.nuevaapptestauxiliarenfermeria.LocalActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onStartTest: (startIndex: Int) -> Unit,
    onReview: () -> Unit,
    onRandomMode: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val savedProgress = PreferencesUtil.getProgress(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "Bienvenido al Test Auxiliar de Enfermería",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (savedProgress > 0) {
                Text(text = "Progreso guardado: Pregunta $savedProgress", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onStartTest(savedProgress) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Continuar Test")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            PreferencesUtil.clearProgress(context)
                            onStartTest(0)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575))
                    ) {
                        Text("Iniciar Nuevo")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                SelectStartButton { selectedIndex ->
                    onStartTest(selectedIndex)
                }
            } else {
                Button(
                    onClick = { onStartTest(0) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Iniciar Test")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onReview() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("Modo Repaso")
                }
                Button(
                    onClick = { onRandomMode() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) {
                    Text("Modo Aleatorio")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { activity.finishAffinity() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text("Salir", textAlign = TextAlign.Center)
            }
        }
        // Parte inferior: Enlace de donación, enlace de contacto y nombre
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Si te ha gustado la aplicación, apoya su desarrollo con un pequeño donativo.",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier.clickable {
                    val donationUrl = "https://www.paypal.com/paypalme/Jucarlos"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(donationUrl))
                    context.startActivity(intent)
                },
                textAlign = TextAlign.Center
            )
            Text(
                text = "Juan Carlos Gómez",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Contacto",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier
                    .clickable {
                        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:AppsCarlos@proton.me"))
                        context.startActivity(emailIntent)
                    }
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center
            )

        }
    }
}
