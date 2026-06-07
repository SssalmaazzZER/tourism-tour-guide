package com.example.tourismguide.domain.model

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val rating: Double,
    val isSaved: Boolean,
    val distanceKm: Double = 0.0
)

data class AuthUser(
    val id: String,
    val name: String?,
    val email: String?,
    val token: String?
)
