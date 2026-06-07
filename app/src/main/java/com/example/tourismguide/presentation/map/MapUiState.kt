package com.example.tourismguide.presentation.map

import android.location.Location
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.domain.model.Place

sealed class LayerState<out T> {
    data object Loading : LayerState<Nothing>()
    data class Success<T>(val data: List<T>) : LayerState<T>()
    data class Error(val message: String) : LayerState<Nothing>()
}

data class UserLocation(val latitude: Double, val longitude: Double) {
    companion object {
        fun from(location: Location) = UserLocation(location.latitude, location.longitude)
    }
}

typealias PlacesState = LayerState<Place>
typealias GuidesState = LayerState<GuideEntity>
