package com.example.deckworkout

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Verwaltet das Umschalten und Speichern der App-Sprache.
 */
object LocaleHelper {
    private const val PREF_NAME = "locale_pref"
    private const val KEY_LANGUAGE = "selected_language"

    fun getLanguage(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun setLanguage(context: Context, languageCode: String): Context {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageCode)
            .apply()
        return updateResources(context, languageCode)
    }

    fun applyLanguage(context: Context): Context {
        val lang = getLanguage(context)
        return updateResources(context, lang)
    }

    private fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
