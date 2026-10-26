package com.example.deckworkout

import android.content.Intent
import android.os.Bundle
import com.example.deckworkout.databinding.ActivityWarmupBinding

class WarmupActivity : BaseActivity() {

    private lateinit var binding: ActivityWarmupBinding
    private var currentStepIndex = 0

    private data class WarmupStep(
        val titleResId: Int,
        val durationResId: Int,
        val descResId: Int,
        val iconResId: Int
    )

    private val warmupSteps = listOf(
        WarmupStep(R.string.warmup_1_title, R.string.warmup_1_duration, R.string.warmup_1_desc, R.drawable.ic_warmup_arms),
        WarmupStep(R.string.warmup_2_title, R.string.warmup_2_duration, R.string.warmup_2_desc, R.drawable.ic_warmup_jacks),
        WarmupStep(R.string.warmup_3_title, R.string.warmup_3_duration, R.string.warmup_3_desc, R.drawable.ic_warmup_torso),
        WarmupStep(R.string.warmup_4_title, R.string.warmup_4_duration, R.string.warmup_4_desc, R.drawable.ic_warmup_wrists)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarmupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showCurrentStep()

        binding.btnNextWarmup.setOnClickListener {
            if (currentStepIndex < warmupSteps.size - 1) {
                currentStepIndex++
                showCurrentStep()
            } else {
                startDeckWorkout()
            }
        }

        binding.btnSkipWarmup.setOnClickListener {
            startDeckWorkout()
        }
    }

    private fun showCurrentStep() {
        val step = warmupSteps[currentStepIndex]

        binding.tvWarmupStep.text = getString(R.string.warmup_step, currentStepIndex + 1, warmupSteps.size)
        binding.ivWarmupIcon.setImageResource(step.iconResId)
        binding.tvWarmupTitle.text = getString(step.titleResId)
        binding.tvWarmupDuration.text = getString(step.durationResId)
        binding.tvWarmupDesc.text = getString(step.descResId)

        if (currentStepIndex == warmupSteps.size - 1) {
            binding.btnNextWarmup.text = getString(R.string.button_start_deck)
        } else {
            binding.btnNextWarmup.text = getString(R.string.button_next_warmup)
        }
    }

    private fun startDeckWorkout() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
