package com.example.deckworkout

import org.json.JSONObject
import java.util.Locale

/**
 * Repräsentiert ein einzelnes absolviertes Workout.
 */
data class WorkoutSession(
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Long = 0L,
    val cardsCompleted: Int,
    val totalCards: Int,
    val pushUps: Int,
    val pullUps: Int,
    val dips: Int,
    val totalReps: Int
) {
    fun formattedDuration(): String {
        if (durationSeconds <= 0L) return "-"
        val minutes = durationSeconds / 60
        val seconds = durationSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d min", minutes, seconds)
    }

    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("timestamp", timestamp)
            put("durationSeconds", durationSeconds)
            put("cardsCompleted", cardsCompleted)
            put("totalCards", totalCards)
            put("pushUps", pushUps)
            put("pullUps", pullUps)
            put("dips", dips)
            put("totalReps", totalReps)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): WorkoutSession {
            return WorkoutSession(
                timestamp = json.optLong("timestamp", System.currentTimeMillis()),
                durationSeconds = json.optLong("durationSeconds", 0L),
                cardsCompleted = json.optInt("cardsCompleted", 0),
                totalCards = json.optInt("totalCards", 52),
                pushUps = json.optInt("pushUps", 0),
                pullUps = json.optInt("pullUps", 0),
                dips = json.optInt("dips", 0),
                totalReps = json.optInt("totalReps", 0)
            )
        }
    }
}
