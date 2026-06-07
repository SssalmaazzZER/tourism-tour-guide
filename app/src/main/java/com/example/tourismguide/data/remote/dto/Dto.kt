package com.example.tourismguide.data.remote.dto

data class PlaceDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val rating: Double
)

data class GuideDto(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val languages: String,
    val rating: Double,
    val pricePerHour: Double,
    val phone: String,
    val specialities: String,
    val isVerified: Boolean,
    val isOnline: Boolean
)

data class ReviewDto(
    val id: String = "",
    val authorId: String,
    val targetId: String,
    val targetType: String,
    val stars: Int,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class BookingDto(
    val id: String = "",
    val userId: String,
    val guideId: String,
    val placeId: String,
    val date: Long,
    val durationHours: Int,
    val status: String = "PENDING",
    val totalAmount: Double
)
