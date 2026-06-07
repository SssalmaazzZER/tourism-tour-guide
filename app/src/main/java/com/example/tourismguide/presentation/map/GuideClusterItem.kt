package com.example.tourismguide.presentation.map

import com.example.tourismguide.data.local.entity.GuideEntity
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class GuideClusterItem(val guide: GuideEntity) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(31.7917, -7.0926)
    override fun getTitle(): String = guide.name
    override fun getSnippet(): String = guide.languages
    override fun getZIndex(): Float = 0f
}
