package com.example.tourismguide.presentation.map

import android.content.Context
import com.example.tourismguide.R
import com.example.tourismguide.util.MapUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CategoryClusterRenderer(
    private val appContext: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<TourismMapClusterItem>
) : DefaultClusterRenderer<TourismMapClusterItem>(appContext, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: TourismMapClusterItem, markerOptions: MarkerOptions) {
        val iconRes = when (item.content.contentType) {
            "GASTRONOMY", "PRODUCT" -> R.drawable.ic_marker_food
            "MONUMENT", "UNESCO", "MUSEUM" -> R.drawable.ic_marker_monument
            "MUSIC", "FESTIVAL", "EVENT" -> R.drawable.ic_mic
            "NATURE", "ACTIVITY" -> R.drawable.ic_map
            else -> R.drawable.ic_place_marker
        }
        markerOptions.icon(MapUtils.vectorToBitmapDescriptor(appContext, iconRes))
    }
}
