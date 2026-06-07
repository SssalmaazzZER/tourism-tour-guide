package com.example.tourismguide.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.tourismguide.util.LocaleHelper
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tourism_preferences")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val sessionToken: Flow<String?> = context.dataStore.data.map { it[SESSION_TOKEN_KEY] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID_KEY] }
    val fcmToken: Flow<String?> = context.dataStore.data.map { it[FCM_TOKEN_KEY] }
    val languagePreference: Flow<String?> = context.dataStore.data.map { it[LANGUAGE_KEY] }
    val darkMode: Flow<Boolean?> = context.dataStore.data.map { it[DARK_MODE_KEY] }
    val notifyNearbyLandmarks: Flow<Boolean?> = context.dataStore.data.map { it[NEARBY_LANDMARKS_KEY] }
    val notifyBookings: Flow<Boolean?> = context.dataStore.data.map { it[BOOKING_UPDATES_KEY] }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
        LocaleHelper.persistLanguage(context, language)
    }

    fun getLanguage(): Flow<String> = context.dataStore.data.map { it[LANGUAGE_KEY] ?: "fr" }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[THEME_KEY] = theme }
    }

    fun getTheme(): Flow<String> = context.dataStore.data.map { it[THEME_KEY] ?: "system" }

    suspend fun setNearbyLandmarksEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NEARBY_LANDMARKS_KEY] = enabled }
    }

    fun getNearbyLandmarksEnabled(): Flow<Boolean> = context.dataStore.data.map { it[NEARBY_LANDMARKS_KEY] ?: true }

    suspend fun setNotifyNearbyLandmarks(enabled: Boolean) {
        context.dataStore.edit { it[NEARBY_LANDMARKS_KEY] = enabled }
    }

    suspend fun setBookingUpdatesEnabled(enabled: Boolean) {
        context.dataStore.edit { it[BOOKING_UPDATES_KEY] = enabled }
    }

    fun getBookingUpdatesEnabled(): Flow<Boolean> = context.dataStore.data.map { it[BOOKING_UPDATES_KEY] ?: true }

    suspend fun setNotifyBookings(enabled: Boolean) {
        context.dataStore.edit { it[BOOKING_UPDATES_KEY] = enabled }
    }

    suspend fun setSessionToken(token: String, userId: String) {
        context.dataStore.edit {
            it[SESSION_TOKEN_KEY] = token
            it[USER_ID_KEY] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it.remove(SESSION_TOKEN_KEY)
            it.remove(USER_ID_KEY)
        }
    }

    suspend fun clearAllUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun setFcmToken(token: String) {
        context.dataStore.edit { it[FCM_TOKEN_KEY] = token }
    }

    fun isFirstLaunch(): Flow<Boolean> = context.dataStore.data.map { it[IS_FIRST_LAUNCH_KEY] ?: true }

    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { it[IS_FIRST_LAUNCH_KEY] = false }
    }

    suspend fun getSessionTokenOnce(): String? = sessionToken.first()

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val THEME_KEY = stringPreferencesKey("theme")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val SESSION_TOKEN_KEY = stringPreferencesKey("session_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val IS_FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
        val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
        val NEARBY_LANDMARKS_KEY = booleanPreferencesKey("nearby_landmarks_enabled")
        val BOOKING_UPDATES_KEY = booleanPreferencesKey("booking_updates_enabled")
    }
}
