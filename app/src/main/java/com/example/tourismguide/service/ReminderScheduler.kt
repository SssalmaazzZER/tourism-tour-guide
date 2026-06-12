package com.example.tourismguide.service

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleFestivalReminder(festivalId: String, festivalName: String, triggerAtMillis: Long) {
        val delay = (triggerAtMillis - System.currentTimeMillis()).coerceAtLeast(0L)
        val input = Data.Builder()
            .putString(FestivalReminderWorker.KEY_FESTIVAL_ID, festivalId)
            .putString(FestivalReminderWorker.KEY_FESTIVAL_NAME, festivalName)
            .build()
        val request = OneTimeWorkRequestBuilder<FestivalReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(input)
            .addTag(FestivalReminderWorker.WORK_TAG)
            .addTag("festival_$festivalId")
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
