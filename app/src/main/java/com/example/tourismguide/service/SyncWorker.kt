package com.example.tourismguide.service

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tourismguide.data.local.AppDatabase
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.mapper.toEntity
import com.example.tourismguide.data.remote.ApiService
import java.io.IOException
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        val database = database()
        val placeDao = database.placeDao()
        val apiService = retrofit().create(ApiService::class.java)

        Timber.d("SyncWorker: Starting periodic sync")
        runCatching {
            apiService.getPlaces().map { it.toEntity() }
        }.onSuccess { places ->
            if (places.isNotEmpty()) {
                placeDao.upsertAll(places)
            }
        }.onFailure { error ->
            Timber.w(error, "SyncWorker: Failed to refresh places")
            if (error is IOException) return Result.retry()
        }

        pruneCache(placeDao)
        Timber.d("SyncWorker: Sync completed successfully")
        Result.success()
    } catch (e: Exception) {
        Timber.e(e, "SyncWorker: Sync failed")
        if (runAttemptCount < 3) Result.retry() else Result.failure()
    }

    private suspend fun pruneCache(placeDao: PlaceDao) {
        try {
            val cutoff = System.currentTimeMillis() - CACHE_TTL_MS
            val deleted = placeDao.deleteOlderThan(cutoff)
            Timber.d("SyncWorker: Deleted %d stale places", deleted)
        } catch (e: Exception) {
            Timber.w(e, "SyncWorker: Failed to prune cache")
        }
    }

    private fun database(): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private companion object {
        const val CACHE_TTL_MS = 7 * 24 * 60 * 60 * 1000L
        const val DB_NAME = "tourism_guide.db"
        const val BASE_URL = "https://example.com/api/"
    }
}
