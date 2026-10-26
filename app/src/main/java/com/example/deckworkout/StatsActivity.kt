package com.example.deckworkout

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.deckworkout.databinding.ActivityStatsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatsActivity : BaseActivity() {

    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isHistoryOnly = intent.getBooleanExtra("history_only", false)

        if (isHistoryOnly) {
            binding.tvMessage.text = getString(R.string.history_title)
            binding.tvCardsCompleted.visibility = View.GONE
            binding.cardStats.visibility = View.GONE
            binding.cardComparison.visibility = View.GONE
        } else {
            val durationSeconds = intent.getLongExtra("duration_seconds", 0L)
            val cardsCompleted = intent.getIntExtra("cards_completed", 0)
            val totalCards = intent.getIntExtra("total_cards", 52)
            val totalPushUps = intent.getIntExtra("total_pushups", 0)
            val totalPullUps = intent.getIntExtra("total_pullups", 0)
            val totalDips = intent.getIntExtra("total_dips", 0)
            val totalReps = intent.getIntExtra("total_reps", 0)

            if (cardsCompleted >= totalCards) {
                binding.tvMessage.text = getString(R.string.congratulations)
            } else {
                binding.tvMessage.text = getString(R.string.workout_finished)
            }

            binding.tvCardsCompleted.text = getString(R.string.cards_completed_label, cardsCompleted, totalCards)

            binding.tvTotalPushUps.text = getString(R.string.repetitions_label, totalPushUps)
            binding.tvTotalPullUps.text = getString(R.string.repetitions_label, totalPullUps)
            binding.tvTotalDips.text = getString(R.string.repetitions_label, totalDips)
            binding.tvTotalRepetitions.text = getString(R.string.repetitions_label, totalReps)

            val dummySessionForFormat = WorkoutSession(durationSeconds = durationSeconds, cardsCompleted = 0, totalCards = 0, pushUps = 0, pullUps = 0, dips = 0, totalReps = 0)
            binding.tvTotalDuration.text = dummySessionForFormat.formattedDuration()

            showComparison(totalReps, cardsCompleted)
        }

        showHistory()

        binding.btnRestart.setOnClickListener {
            finish()
        }
    }

    private fun showComparison(currentReps: Int, currentCards: Int) {
        val previous = WorkoutHistoryManager.getPreviousSession(this)
        if (previous != null) {
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val prevDate = sdf.format(Date(previous.timestamp))

            val repDiff = currentReps - previous.totalReps
            val cardDiff = currentCards - previous.cardsCompleted

            val repStr = when {
                repDiff > 0 -> getString(R.string.trend_reps_plus, repDiff)
                repDiff < 0 -> getString(R.string.trend_reps_minus, kotlin.math.abs(repDiff))
                else -> getString(R.string.trend_reps_equal)
            }

            val cardStr = when {
                cardDiff > 0 -> getString(R.string.trend_cards_plus, cardDiff)
                cardDiff < 0 -> getString(R.string.trend_cards_minus, kotlin.math.abs(cardDiff))
                else -> getString(R.string.trend_cards_equal)
            }

            val prevDuration = previous.formattedDuration()
            binding.tvComparisonText.text = getString(
                R.string.comparison_summary,
                prevDate,
                previous.totalReps,
                previous.cardsCompleted,
                prevDuration,
                repStr,
                cardStr
            )
        } else {
            binding.tvComparisonText.text = getString(R.string.comparison_first)
        }
    }

    private fun showHistory() {
        val sessions = WorkoutHistoryManager.getSessions(this)
        binding.containerHistory.removeAllViews()

        if (sessions.isEmpty()) {
            val tvEmpty = TextView(this).apply {
                text = getString(R.string.history_empty)
                textSize = 14f
                setTextColor(resources.getColor(R.color.text_secondary, theme))
            }
            binding.containerHistory.addView(tvEmpty)
            return
        }

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        for ((index, session) in sessions.take(10).withIndex()) {
            val dateStr = sdf.format(Date(session.timestamp))
            val durStr = session.formattedDuration()

            val historyText = getString(
                R.string.history_item,
                dateStr,
                durStr,
                session.cardsCompleted,
                session.totalCards,
                session.totalReps,
                session.pushUps,
                session.pullUps,
                session.dips
            )

            val itemView = TextView(this).apply {
                textSize = 14f
                setTextColor(resources.getColor(R.color.text_primary, theme))
                setPadding(0, 12, 0, 12)
                text = historyText
            }
            binding.containerHistory.addView(itemView)

            if (index < sessions.take(10).size - 1) {
                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        setMargins(0, 8, 0, 8)
                    }
                    setBackgroundColor(resources.getColor(R.color.divider, theme))
                }
                binding.containerHistory.addView(divider)
            }
        }
    }
}
