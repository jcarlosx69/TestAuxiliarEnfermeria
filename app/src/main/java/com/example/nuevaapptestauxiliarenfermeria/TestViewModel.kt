package com.example.nuevaapptestauxiliarenfermeria

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data class para almacenar estadísticas de cada pregunta.
data class QuestionStat(
    val question: Question,
    var correct: Int = 0,
    var wrong: Int = 0
)

class TestViewModel(application: Application) : AndroidViewModel(application) {

    // Lista de preguntas (se cargan desde el archivo de assets).
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: List<Question>
        get() = _questions.value

    // Índice de la pregunta actual, inicializado con el progreso guardado.
    private val _currentIndex = MutableStateFlow(PreferencesUtil.getProgress(getApplication()))
    val currentIndex = _currentIndex.asStateFlow()

    // Respuesta seleccionada (null si no se ha seleccionado).
    private val _selectedAnswer = MutableStateFlow<Char?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

    // Indica si la pregunta ya fue respondida (para mostrar feedback).
    private val _answered = MutableStateFlow(false)
    val answered = _answered.asStateFlow()

    // Contadores globales de respuestas correctas e incorrectas.
    private val _correctCount = MutableStateFlow(0)
    val correctCount = _correctCount.asStateFlow()

    private val _wrongCount = MutableStateFlow(0)
    val wrongCount = _wrongCount.asStateFlow()

    // Mapa para almacenar las estadísticas individuales por pregunta (clave: número de pregunta).
    private val _questionStats = mutableMapOf<Int, QuestionStat>()
    val questionStats: Map<Int, QuestionStat> get() = _questionStats

    // Obtenemos SharedPreferences para persistir las estadísticas.
    private val prefs = getApplication<Application>().getSharedPreferences("TestStats", Context.MODE_PRIVATE)

    init {
        loadQuestions()
        loadQuestionStats() // Cargamos estadísticas persistidas, si existen.
    }

    private fun loadQuestions() {
        val context = getApplication<Application>().applicationContext
        val fileContent = readPreguntasFromAssets(context)
        _questions.value = parseQuestions(fileContent)
    }

    private fun parseQuestions(fileContent: String): List<Question> {
        val questions = mutableListOf<Question>()
        // Separamos el contenido en bloques usando uno o más saltos de línea en blanco
        val blocks = fileContent.split(Regex("\\n\\s*\\n"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        for (block in blocks) {
            // Dividimos cada bloque en líneas y eliminamos las que estén vacías
            val lines = block.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if (lines.size < 6) continue  // Si el bloque no tiene al menos 6 líneas, lo ignoramos

            // Línea 1: Extraemos el número y el enunciado usando una expresión regular.
            val headerRegex = Regex("^([0-9]+)\\.\\s*(.*)")
            val headerMatch = headerRegex.find(lines[0]) ?: continue
            val number = headerMatch.groupValues[1].toIntOrNull() ?: 0
            val enunciado = headerMatch.groupValues[2].trim()

            // Función auxiliar para limpiar cada opción (elimina prefijos como "A) " o "A) A)")
            fun cleanOption(line: String): String {
                var result = line.replace(Regex("^[A-D]\\)\\s*[A-D]\\)\\s*"), "")
                if (result == line) {
                    result = line.replace(Regex("^[A-D]\\)\\s*"), "")
                }
                return result.trim()
            }

            // Líneas 2 a 5: Opciones A, B, C y D
            val optionA = cleanOption(lines[1])
            val optionB = cleanOption(lines[2])
            val optionC = cleanOption(lines[3])
            val optionD = cleanOption(lines[4])

            // Línea 6: Extraemos la respuesta correcta utilizando una expresión regular que admite variantes:
            // "Respuesta Correcta:" o "La respuesta correcta es:"
            val answerRegex = Regex("(?i)(?:La\\s+)?respuesta\\s+correcta(?:\\s+es)?\\s*:\\s*([A-D])")
            val answerMatch = answerRegex.find(lines[5])
            val correctAnswer = answerMatch?.groupValues?.get(1)?.firstOrNull() ?: 'A'

            questions.add(Question(number, enunciado, optionA, optionB, optionC, optionD, correctAnswer))
        }

        return questions
    }


    fun selectAnswer(answer: Char) {
        if (!_answered.value) {
            _selectedAnswer.value = answer
            _answered.value = true
            val currentQuestion = questions.getOrNull(_currentIndex.value)
            if (currentQuestion != null) {
                val stat = _questionStats.getOrPut(currentQuestion.number) {
                    QuestionStat(currentQuestion)
                }
                if (answer.uppercaseChar() == currentQuestion.correctAnswer.uppercaseChar()) {
                    stat.correct += 1
                    _correctCount.value += 1
                } else {
                    stat.wrong += 1
                    _wrongCount.value += 1
                }
                // Persistimos las estadísticas actualizadas.
                saveQuestionStats()
            }
        }
    }

    fun nextQuestion() {
        if (_currentIndex.value < questions.size - 1) {
            _currentIndex.value += 1
            PreferencesUtil.saveProgress(getApplication(), _currentIndex.value)
            _selectedAnswer.value = null
            _answered.value = false
        }
    }

    fun setProgress(newIndex: Int) {
        _currentIndex.value = newIndex
        PreferencesUtil.saveProgress(getApplication(), newIndex)
    }

    fun resetTest() {
        _currentIndex.value = 0
        _selectedAnswer.value = null
        _answered.value = false
        _correctCount.value = 0
        _wrongCount.value = 0
        _questionStats.clear()
        saveQuestionStats() // Actualizamos el almacenamiento (limpiando las estadísticas)
        PreferencesUtil.clearProgress(getApplication())
    }

    private fun saveQuestionStats() {
        val gson = Gson()
        val json = gson.toJson(_questionStats)
        prefs.edit().putString("questionStats", json).apply()
    }

    private fun loadQuestionStats() {
        val gson = Gson()
        val json = prefs.getString("questionStats", null)
        if (json != null) {
            val type = object : TypeToken<MutableMap<Int, QuestionStat>>() {}.type
            val map: MutableMap<Int, QuestionStat>? = gson.fromJson(json, type)
            if (map != null) {
                _questionStats.putAll(map)
            }
        }
    }

    fun getReviewQuestions(): List<QuestionStat> {
        return _questionStats.values.filter { it.wrong > 0 }
            .sortedByDescending { it.wrong }
    }
}
