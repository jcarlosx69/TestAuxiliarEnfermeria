package com.example.nuevaapptestauxiliarenfermeria

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnswerOption(
    optionLetter: Char,
    optionText: String,
    isSelected: Boolean,
    correctAnswer: Char,
    answered: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = when {
        answered && optionLetter == correctAnswer -> Color(0xFFA5D6A7)
        answered && isSelected && optionLetter != correctAnswer -> Color(0xFFEF9A9A)
        else -> Color.White
    }
    val targetScale = if (answered && optionLetter == correctAnswer) 1.1f else 1.0f
    val scale by animateFloatAsState(targetValue = targetScale)
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(enabled = !answered, onClick = onSelect)
            .scale(scale)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = androidx.compose.ui.Alignment.Start
        ) {
            Text(
                text = "$optionLetter)",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = optionText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}
