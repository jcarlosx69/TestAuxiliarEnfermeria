package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.startActivity

@Composable
fun HomeScreen(onStartTest: (startIndex: Int) -> Unit, onReview: () -> Unit) {
    val context = LocalContext.current
    val savedProgress = PreferencesUtil.getProgress(context)
    // Definimos el enlace directo a PayPal.me
    val donationUrl = "https://www.paypal.me/Jucarlos"
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "Bienvenido al Test para Auxiliares de Enfermería.",
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
                    Button(onClick = { onStartTest(savedProgress) }) {
                        Text("Continuar Test")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
                        PreferencesUtil.clearProgress(context)
                        onStartTest(0)
                    }) {
                        Text("Iniciar Nuevo")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                SelectStartButton { selectedIndex ->
                    onStartTest(selectedIndex)
                }
            } else {
                Button(onClick = { onStartTest(0) }) {
                    Text("Iniciar Test")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onReview() }) {
                Text("Modo Repaso")
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Si disfrutas de esta app y te ha sido útil, puedes invitarme a un café haciendo clic aquí.",
                color = Color.Blue,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(donationUrl))
                        context.startActivity(intent)
                    }
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Juan Carlos",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "SUGERENCIAS.",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable {
                        // Al pulsar se abre el cliente de correo con el email preconfigurado
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:AppsCarlos@proton.me")
                        }
                        context.startActivity(emailIntent)
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}