package com.example.tourismguide.presentation.map

import com.example.tourismguide.domain.model.TourismContent
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class TourismMapClusterItem(val content: TourismContent) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(content.latitude, content.longitude)
    override fun getTitle(): String = content.title
    override fun getSnippet(): String = content.subtitle.ifBlank { content.contentType }
    override fun getZIndex(): Float? = null
}
