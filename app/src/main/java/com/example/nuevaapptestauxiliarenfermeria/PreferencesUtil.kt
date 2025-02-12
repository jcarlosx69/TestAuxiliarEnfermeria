package com.example.nuevaapptestauxiliarenfermeria

import android.content.Context

object PreferencesUtil {
    private const val PREFS_NAME = "test_prefs"
    private const val KEY_PROGRESS = "progress"

    fun getProgress(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_PROGRESS, 0)
    }

    fun saveProgress(context: Context, progress: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_PROGRESS, progress).apply()
    }

    fun clearProgress(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_PROGRESS).apply()
    }
}
