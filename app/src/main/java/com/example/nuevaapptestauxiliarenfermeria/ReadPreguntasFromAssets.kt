package com.example.nuevaapptestauxiliarenfermeria

import android.content.Context

/**
 * Lee el contenido del archivo "preguntas.txt" ubicado en la carpeta assets.
 *
 * @param context Contexto de la aplicación.
 * @return Una cadena con todo el contenido del archivo o una cadena vacía en caso de error.
 */
fun readPreguntasFromAssets(context: Context): String {
    return try {
        context.assets.open("preguntas.txt").bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
