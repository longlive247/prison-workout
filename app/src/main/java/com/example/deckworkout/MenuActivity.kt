package com.example.deckworkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.deckworkout.databinding.ActivityMenuBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuActivity : BaseActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguageButtons()
        setupRestSettingButton()

        binding.btnStartWorkout.setOnClickListener {
            val intent = Intent(this, WarmupActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewHistory.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java).apply {
                putExtra("history_only", true)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateLastWorkoutCard()
        updateRestSettingButtonText()
    }

    private fun setupLanguageButtons() {
        val currentLang = LocaleHelper.getLanguage(this)
        updateLanguageButtonStates(currentLang)

        binding.btnLangEn.setOnClickListener {
            if (currentLang != "en") {
                LocaleHelper.setLanguage(this, "en")
                recreate()
            }
        }

        binding.btnLangDe.setOnClickListener {
            if (currentLang != "de") {
                LocaleHelper.setLanguage(this, "de")
                recreate()
            }
        }
    }

    private fun updateLanguageButtonStates(lang: String) {
        val activeColor = resources.getColor(R.color.colorAccent, theme)
        val inactiveColor = resources.getColor(R.color.text_secondary, theme)

        if (lang == "de") {
            binding.btnLangDe.setBackgroundColor(activeColor)
            binding.btnLangDe.setTextColor(resources.getColor(R.color.background, theme))
            binding.btnLangEn.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            binding.btnLangEn.setTextColor(inactiveColor)
        } else {
            binding.btnLangEn.setBackgroundColor(activeColor)
            binding.btnLangEn.setTextColor(resources.getColor(R.color.background, theme))
            binding.btnLangDe.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            binding.btnLangDe.setTextColor(inactiveColor)
        }
    }

    private fun setupRestSettingButton() {
        updateRestSettingButtonText()
        binding.btnRestSetting.setOnClickListener {
            showRestSettingDialog()
        }
    }

    private fun updateRestSettingButtonText() {
        val currentSeconds = RestTimerHelper.getRestDurationSeconds(this)
        if (currentSeconds > 0) {
            binding.btnRestSetting.text = getString(R.string.rest_setting_label, currentSeconds)
        } else {
            binding.btnRestSetting.text = getString(R.string.rest_setting_off)
        }
    }

    private fun showRestSettingDialog() {
        val options = arrayOf(
            getString(R.string.option_rest_off),
            getString(R.string.option_rest_30),
            getString(R.string.option_rest_45),
            getString(R.string.option_rest_60),
            getString(R.string.option_rest_90)
        )
        val values = intArrayOf(0, 30, 45, 60, 90)
        val currentSeconds = RestTimerHelper.getRestDurationSeconds(this)
        var selectedIndex = values.indexOf(currentSeconds)
        if (selectedIndex == -1) selectedIndex = 2 // Standard 45s

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.rest_setting_dialog_title))
            .setSingleChoiceItems(options, selectedIndex) { dialog, which ->
                RestTimerHelper.setRestDurationSeconds(this, values[which])
                updateRestSettingButtonText()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun updateLastWorkoutCard() {
        val sessions = WorkoutHistoryManager.getSessions(this)
        if (sessions.isNotEmpty()) {
            val last = sessions.first()
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val dateStr = sdf.format(Date(last.timestamp))
            val durStr = last.formattedDuration()
            binding.tvLastWorkoutInfo.text = getString(
                R.string.last_workout_summary,
                dateStr,
                durStr,
                last.cardsCompleted,
                last.totalCards,
                last.totalReps,
                last.pushUps,
                last.pullUps,
                last.dips
            )
        } else {
            binding.tvLastWorkoutInfo.text = getString(R.string.last_workout_none)
        }
    }
}
