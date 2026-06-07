package com.example.tourismguide.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int = 0,
    val address: String = "",
    val placeType: String = "attraction",
    val isSaved: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis(),
    val lastAccessedAt: Long = 0L
)

@Entity(tableName = "guides")
data class GuideEntity(
    @PrimaryKey val id: String,
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

@Entity(tableName = "itineraries")
data class ItineraryEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val placeIds: String,
    val startDate: Long,
    val createdAt: Long
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val guideId: String,
    val placeId: String,
    val date: Long,
    val durationHours: Int,
    val status: String,
    val totalAmount: Double
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val targetId: String,
    val targetType: String,
    val stars: Int,
    val text: String,
    val createdAt: Long
)

@Entity(tableName = "user_photos")
data class UserPhotoEntity(
    @PrimaryKey val id: String,
    val placeId: String,
    val userId: String,
    val localPath: String,
    val remoteUrl: String?,
    val takenAt: Long
)

@Entity(tableName = "guide_requests")
data class GuideRequestEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val guideId: String,
    val status: String,
    val requestedDate: Long,
    val durationHours: Int,
    val peopleCount: Int,
    val startLat: Double,
    val startLng: Double,
    val specialRequests: String,
    val estimatedPrice: Double,
    val createdAt: Long
)
