package com.example.tourismguide.domain.model

import com.google.firebase.Timestamp

data class GuideLocation(
    val guideId: String,
    val latitude: Double,
    val longitude: Double,
    val isOnline: Boolean,
    val lastSeen: Timestamp
)
