package com.example.deckworkout

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

/**
 * Basis-Klasse für alle Aktivitäten, um die gewählte Sprache automatisch zu übernehmen.
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLanguage(newBase))
    }
}
