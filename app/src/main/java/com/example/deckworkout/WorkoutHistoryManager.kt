package com.example.deckworkout

import android.content.Context
import org.json.JSONArray

/**
 * Speichert und lädt die Workout-Historie in den SharedPreferences.
 */
object WorkoutHistoryManager {
    private const val PREF_NAME = "workout_history_pref"
    private const val KEY_SESSIONS = "sessions_json"

    fun saveSession(context: Context, session: WorkoutSession) {
        val sessions = getSessions(context).toMutableList()
        sessions.add(0, session) // Neueste Zuerst
        val jsonArray = JSONArray()
        for (s in sessions) {
            jsonArray.put(s.toJson())
        }
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SESSIONS, jsonArray.toString())
            .apply()
    }

    fun getSessions(context: Context): List<WorkoutSession> {
        val jsonString = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_SESSIONS, null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<WorkoutSession>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(WorkoutSession.fromJson(obj))
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getPreviousSession(context: Context): WorkoutSession? {
        val sessions = getSessions(context)
        // Wenn ein Workout frisch gespeichert wurde, liegt das aktuelle bei Index 0
        // und das vorherige bei Index 1.
        return if (sessions.size > 1) sessions[1] else null
    }
}
