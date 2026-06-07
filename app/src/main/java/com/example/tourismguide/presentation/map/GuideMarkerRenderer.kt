package com.example.tourismguide.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class GuideMarkerRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<GuideClusterItem>
) : DefaultClusterRenderer<GuideClusterItem>(context, map, clusterManager) {
    override fun onBeforeClusterItemRendered(item: GuideClusterItem, markerOptions: MarkerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createGuideMarker(item.guide.name)))
    }

    private fun createGuideMarker(name: String): Bitmap {
        val bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.rgb(24, 160, 88)
        canvas.drawCircle(48f, 48f, 42f, paint)
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText(name.take(2).uppercase(), 48f, 58f, paint)
        return bitmap
    }
}
