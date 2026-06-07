package com.example.tourismguide.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "tourism_locale_prefs"
    private const val KEY_LANGUAGE = "language"

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = when (languageCode.lowercase(Locale.ROOT)) {
            "ar" -> Locale("ar")
            "fr" -> Locale.FRENCH
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    /**
     * Used in attachBaseContext for synchronous locale wrapping.
     * Uses SharedPreferences because DataStore is not available this early in app lifecycle.
     */
    fun wrapContext(context: Context): Context {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val language = prefs.getString(KEY_LANGUAGE, "fr") ?: "fr"
        return setLocale(context, language)
    }

    /**
     * Persists the language to SharedPreferences to ensure wrapContext has access on next boot.
     */
    fun persistLanguage(context: Context, language: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
    }
}
