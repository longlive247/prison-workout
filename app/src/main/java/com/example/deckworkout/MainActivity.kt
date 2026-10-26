package com.example.deckworkout

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import com.example.deckworkout.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var workoutManager: WorkoutManager

    private var restTimer: CountDownTimer? = null
    private var restTimeRemainingMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workoutManager = WorkoutManager()

        // Timer starten
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.start()

        workoutManager.nextCard()
        showCurrentCard()

        binding.btnNext.setOnClickListener {
            val customReps = getEnteredCustomReps()
            workoutManager.completeCurrentCard(customReps)
            binding.etCustomReps.text?.clear()

            if (workoutManager.hasNext()) {
                val restSeconds = RestTimerHelper.getRestDurationSeconds(this)
                if (restSeconds > 0) {
                    startRestTimer(restSeconds)
                } else {
                    workoutManager.nextCard()
                    showCurrentCard()
                }
            } else {
                openStats()
            }
        }

        binding.btnFinish.setOnClickListener {
            val customReps = getEnteredCustomReps()
            workoutManager.completeCurrentCard(customReps)
            binding.etCustomReps.text?.clear()
            cancelRestTimer()
            openStats()
        }

        binding.btnSkipRest.setOnClickListener {
            finishRestAndNextCard()
        }

        binding.btnRestPlus15.setOnClickListener {
            restTimeRemainingMillis += 15000L
            runRestCountDown(restTimeRemainingMillis)
        }

        binding.btnRestMinus15.setOnClickListener {
            if (restTimeRemainingMillis > 15000L) {
                restTimeRemainingMillis -= 15000L
                runRestCountDown(restTimeRemainingMillis)
            } else {
                finishRestAndNextCard()
            }
        }
    }

    private fun startRestTimer(seconds: Int) {
        binding.layoutRestOverlay.visibility = View.VISIBLE
        binding.cardView.visibility = View.GONE
        binding.tvExercise.visibility = View.GONE
        binding.tvRepetitions.visibility = View.GONE
        binding.tilCustomReps.visibility = View.GONE

        runRestCountDown(seconds * 1000L)
    }

    private fun runRestCountDown(millis: Long) {
        restTimer?.cancel()
        restTimeRemainingMillis = millis

        restTimer = object : CountDownTimer(millis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                restTimeRemainingMillis = millisUntilFinished
                val totalSec = (millisUntilFinished / 1000L).toInt()
                val min = totalSec / 60
                val sec = totalSec % 60
                binding.tvRestCountdown.text = String.format(Locale.getDefault(), "%02d:%02d", min, sec)
            }

            override fun onFinish() {
                vibrateShort()
                finishRestAndNextCard()
            }
        }.start()
    }

    private fun finishRestAndNextCard() {
        cancelRestTimer()

        binding.layoutRestOverlay.visibility = View.GONE
        binding.cardView.visibility = View.VISIBLE
        binding.tvExercise.visibility = View.VISIBLE
        binding.tvRepetitions.visibility = View.VISIBLE
        binding.tilCustomReps.visibility = View.VISIBLE

        workoutManager.nextCard()
        showCurrentCard()
    }

    private fun cancelRestTimer() {
        restTimer?.cancel()
        restTimer = null
    }

    private fun vibrateShort() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(300)
                }
            }
        } catch (_: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelRestTimer()
    }

    private fun getEnteredCustomReps(): Int? {
        val input = binding.etCustomReps.text?.toString()?.trim()
        return if (!input.isNullOrEmpty()) {
            input.toIntOrNull()
        } else {
            null
        }
    }

    private fun showCurrentCard() {
        val card = workoutManager.currentCard() ?: return

        binding.tvCardValue.text = card.displayName()
        binding.tvCardValue.setTextColor(resources.getColor(card.colorResId(), theme))
        binding.tvSuit.text = card.suit.symbol
        binding.tvExercise.text = getString(card.exercise.nameResId)
        binding.tvRepetitions.text = getString(R.string.repetitions_label, card.repetitions())

        binding.tilCustomReps.hint = getString(R.string.custom_reps_hint_default, card.repetitions())

        binding.tvProgress.text = getString(
            R.string.progress_label,
            workoutManager.currentCardIndex() + 1,
            workoutManager.getTotalCards()
        )
    }

    private fun openStats() {
        val elapsedMillis = SystemClock.elapsedRealtime() - binding.chronometer.base
        val durationSeconds = elapsedMillis / 1000
        binding.chronometer.stop()

        val cardsCompleted = workoutManager.getCompletedCardsCount()
        val totalCards = workoutManager.getTotalCards()
        val pushUps = workoutManager.getTotalPushUps()
        val pullUps = workoutManager.getTotalPullUps()
        val dips = workoutManager.getTotalDips()
        val totalReps = workoutManager.getTotalRepetitions()

        val session = WorkoutSession(
            durationSeconds = durationSeconds,
            cardsCompleted = cardsCompleted,
            totalCards = totalCards,
            pushUps = pushUps,
            pullUps = pullUps,
            dips = dips,
            totalReps = totalReps
        )

        // Workout-Session dauerhaft speichern
        WorkoutHistoryManager.saveSession(this, session)

        val intent = Intent(this, StatsActivity::class.java).apply {
            putExtra("duration_seconds", durationSeconds)
            putExtra("cards_completed", cardsCompleted)
            putExtra("total_cards", totalCards)
            putExtra("total_pushups", pushUps)
            putExtra("total_pullups", pullUps)
            putExtra("total_dips", dips)
            putExtra("total_reps", totalReps)
        }
        startActivity(intent)
        finish()
    }
}
