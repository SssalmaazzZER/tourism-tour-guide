package com.example.tourismguide.presentation.map

import com.example.tourismguide.domain.model.Place
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class PlaceClusterItem(val place: Place, private val openLabel: String) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(place.latitude, place.longitude)
    override fun getTitle(): String = place.name
    override fun getSnippet(): String = openLabel
    override fun getZIndex(): Float = 0f
}
