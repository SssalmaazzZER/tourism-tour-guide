package com.example.tourismguide.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tourismguide.util.NotificationHelper

class FestivalReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val festivalId = inputData.getString(KEY_FESTIVAL_ID).orEmpty()
        val festivalName = inputData.getString(KEY_FESTIVAL_NAME).orEmpty()
        if (festivalId.isBlank()) return Result.failure()
        NotificationHelper(applicationContext).showFestivalReminder(festivalId, festivalName)
        return Result.success()
    }

    companion object {
        const val KEY_FESTIVAL_ID = "festival_id"
        const val KEY_FESTIVAL_NAME = "festival_name"
        const val WORK_TAG = "festival_reminder"
    }
}
