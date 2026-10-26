package com.example.deckworkout

import android.content.Context

/**
 * Speichert und lädt die konfigurierte Pausenzeit in den SharedPreferences.
 */
object RestTimerHelper {
    private const val PREF_NAME = "rest_timer_pref"
    private const val KEY_REST_SECONDS = "rest_seconds"

    fun getRestDurationSeconds(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_REST_SECONDS, 45) // Standard: 45 Sekunden
    }

    fun setRestDurationSeconds(context: Context, seconds: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_REST_SECONDS, seconds)
            .apply()
    }
}
