package com.example.tourismguide.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews
import androidx.room.Room
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.tourismguide.R
import com.example.tourismguide.data.local.AppDatabase
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.presentation.place.PlaceDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class DestinationWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Timber.d("DestinationWidgetProvider: updating %d widgets", appWidgetIds.size)
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO + Job()).launch {
            try {
                val placeDao = database(context).placeDao()
                val place = placeDao.getRandomUnsavedPlace() ?: placeDao.getAll().firstOrNull()
                val bitmap = place?.imageUrl?.takeIf { it.isNotBlank() }?.let { loadBitmap(context, it) }
                updateWidgets(context, appWidgetManager, appWidgetIds, place, bitmap)
            } catch (e: Exception) {
                Timber.e(e, "DestinationWidgetProvider: failed to update widget")
                updateFallback(context, appWidgetManager, appWidgetIds)
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        updateAllWidgets(context)
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, DestinationWidgetProvider::class.java))
            updateFallback(context, manager, ids)
        }

        private fun updateWidgets(
            context: Context,
            manager: AppWidgetManager,
            appWidgetIds: IntArray,
            place: PlaceEntity?,
            bitmap: Bitmap?
        ) {
            appWidgetIds.forEach { appWidgetId ->
                val views = RemoteViews(context.packageName, R.layout.widget_destination)
                if (place != null) {
                    views.setTextViewText(R.id.widgetTitle, context.getString(R.string.app_name))
                    views.setTextViewText(R.id.widgetPlaceCount, place.category)
                    views.setTextViewText(R.id.widgetPlaceName, place.name)
                    views.setImageViewBitmap(R.id.widgetPlaceImage, bitmap ?: placeholderBitmap(context))
                    val intent = Intent(context, PlaceDetailActivity::class.java).apply {
                        putExtra("placeId", place.id)
                    }
                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        place.id.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent)
                } else {
                    views.setTextViewText(R.id.widgetTitle, context.getString(R.string.app_name))
                    views.setTextViewText(R.id.widgetPlaceCount, context.getString(R.string.coming_soon))
                    views.setTextViewText(R.id.widgetPlaceName, context.getString(R.string.no_places_found))
                    views.setImageViewResource(R.id.widgetPlaceImage, R.drawable.ic_placeholder)
                }
                manager.updateAppWidget(appWidgetId, views)
            }
        }

        private fun updateFallback(
            context: Context,
            manager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            appWidgetIds.forEach { appWidgetId ->
                val views = RemoteViews(context.packageName, R.layout.widget_destination)
                views.setTextViewText(R.id.widgetTitle, context.getString(R.string.app_name))
                views.setTextViewText(R.id.widgetPlaceCount, context.getString(R.string.coming_soon))
                views.setTextViewText(R.id.widgetPlaceName, context.getString(R.string.no_places_found))
                views.setImageViewResource(R.id.widgetPlaceImage, R.drawable.ic_placeholder)
                manager.updateAppWidget(appWidgetId, views)
            }
        }

        private suspend fun loadBitmap(context: Context, url: String): Bitmap? {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()
            return when (val result = loader.execute(request)) {
                is SuccessResult -> (result.drawable as? BitmapDrawable)?.bitmap
                else -> null
            }
        }

        private fun placeholderBitmap(context: Context): Bitmap? {
            val drawable = context.getDrawable(R.drawable.ic_placeholder) ?: return null
            val width = drawable.intrinsicWidth.coerceAtLeast(1)
            val height = drawable.intrinsicHeight.coerceAtLeast(1)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        private fun database(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        ).build()

        private const val DB_NAME = "tourism_guide.db"
    }
}
