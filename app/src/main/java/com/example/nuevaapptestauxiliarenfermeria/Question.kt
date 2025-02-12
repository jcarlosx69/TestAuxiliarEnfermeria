package com.example.nuevaapptestauxiliarenfermeria

data class Question(
    val number: Int,
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: Char
)