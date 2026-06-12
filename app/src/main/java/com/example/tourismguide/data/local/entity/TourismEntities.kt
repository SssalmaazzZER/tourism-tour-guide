package com.example.tourismguide.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val preferredLanguage: String = "fr",
    val favoriteCategories: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "regions")
data class RegionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageUrl: String = ""
)

@Entity(
    tableName = "cities",
    foreignKeys = [
        ForeignKey(
            entity = RegionEntity::class,
            parentColumns = ["id"],
            childColumns = ["regionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("regionId")]
)
data class CityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val regionId: String?,
    val population: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val climate: String = "",
    val history: String = "",
    val imageUrl: String,
    val rating: Double = 4.5,
    val popularity: Int = 0
)

@Entity(
    tableName = "monuments",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("cityId")]
)
data class MonumentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val history: String = "",
    val cityId: String?,
    val latitude: Double,
    val longitude: Double,
    val entryPrice: String = "",
    val openingHours: String = "",
    val monumentType: String = "",
    val imageUrl: String,
    val rating: Double = 4.5,
    val popularity: Int = 0
)

@Entity(tableName = "points_of_interest")
data class PointOfInterestEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val cityId: String?,
    val latitude: Double,
    val longitude: Double,
    val poiType: String = "",
    val imageUrl: String,
    val rating: Double = 4.0
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String = "",
    val sortOrder: Int = 0
)

@Entity(tableName = "culture_items")
data class CultureEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val history: String = "",
    val cultureType: String = "",
    val imageUrl: String,
    val regionId: String? = null
)

@Entity(tableName = "architecture_items")
data class ArchitectureEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val history: String = "",
    val architectureType: String = "",
    val regionId: String? = null,
    val imageUrl: String,
    val relatedMonumentIds: String = ""
)

@Entity(tableName = "music_styles")
data class MusicStyleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val history: String = "",
    val instruments: String = "",
    val regions: String = "",
    val famousArtists: String = "",
    val imageUrl: String
)

@Entity(tableName = "instruments")
data class InstrumentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String = "",
    val musicStyleId: String? = null
)

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val history: String = "",
    val ingredients: String = "",
    val regionId: String? = null,
    val dishType: String = "dish",
    val imageUrl: String,
    val rating: Double = 4.5
)

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val dishId: String? = null
)

@Entity(tableName = "local_products")
data class LocalProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val history: String = "",
    val regionId: String? = null,
    val imageUrl: String
)

@Entity(
    tableName = "festivals",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("cityId")]
)
data class FestivalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val dates: String,
    val program: String = "",
    val cityId: String?,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)

@Entity(tableName = "artisanat_items")
data class ArtisanatEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val craftType: String = "",
    val regionId: String? = null,
    val imageUrl: String
)

@Entity(tableName = "nature_sites")
data class NatureSiteEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val natureType: String = "",
    val regionId: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val rating: Double = 4.5
)

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val activityType: String = "",
    val regionId: String? = null,
    val latitude: Double,
    val longitude: Double,
    val priceRange: String = "",
    val imageUrl: String,
    val rating: Double = 4.5
)

@Entity(
    tableName = "museums",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("cityId")]
)
data class MuseumEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val history: String = "",
    val cityId: String?,
    val latitude: Double,
    val longitude: Double,
    val entryPrice: String = "",
    val openingHours: String = "",
    val imageUrl: String,
    val rating: Double = 4.5
)

@Entity(tableName = "unesco_heritage")
data class UnescoHeritageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val yearInscribed: Int = 0,
    val cityId: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("cityId")]
)
data class EventEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val eventType: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val cityId: String?,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)

@Entity(tableName = "circuits")
data class CircuitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val circuitType: String = "",
    val durationDays: Int = 1,
    val imageUrl: String,
    val isPreset: Boolean = true
)

@Entity(
    tableName = "circuit_steps",
    foreignKeys = [
        ForeignKey(
            entity = CircuitEntity::class,
            parentColumns = ["id"],
            childColumns = ["circuitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("circuitId")]
)
data class CircuitStepEntity(
    @PrimaryKey val id: String,
    val circuitId: String,
    val stepOrder: Int,
    val title: String,
    val description: String = "",
    val referenceType: String = "",
    val referenceId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val targetType: String,
    val targetId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey val id: String,
    val query: String,
    val searchedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "recently_viewed")
data class RecentlyViewedEntity(
    @PrimaryKey val id: String,
    val targetType: String,
    val targetId: String,
    val title: String,
    val imageUrl: String = "",
    val viewedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val notificationType: String = "",
    val targetId: String = "",
    val scheduledAt: Long = 0,
    val isRead: Boolean = false
)

@Entity(
    tableName = "tourism_offices",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("cityId")]
)
data class TourismOfficeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cityId: String?,
    val phone: String = "",
    val email: String = "",
    val website: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Entity(tableName = "recommendations")
data class RecommendationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String = "",
    val targetType: String,
    val targetId: String,
    val score: Double = 0.0,
    val reason: String = ""
)

@Entity(tableName = "content_rich_details")
data class ContentRichDetailEntity(
    @PrimaryKey val contentId: String,
    val richBody: String,
    val galleryUrls: String = "",
    val leadersJson: String = ""
)

@Entity(
    tableName = "user_itinerary_days",
    foreignKeys = [
        ForeignKey(
            entity = ItineraryEntity::class,
            parentColumns = ["id"],
            childColumns = ["itineraryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("itineraryId")]
)
data class UserItineraryDayEntity(
    @PrimaryKey val id: String,
    val itineraryId: String,
    val dayOrder: Int,
    val title: String,
    val description: String = "",
    val contentId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Entity(tableName = "scheduled_reminders")
data class ScheduledReminderEntity(
    @PrimaryKey val id: String,
    val targetType: String,
    val targetId: String,
    val title: String,
    val triggerAt: Long,
    val isFired: Boolean = false
)

@Entity(tableName = "content_index")
data class ContentIndexEntity(
    @PrimaryKey val id: String,
    val contentType: String,
    val title: String,
    val subtitle: String = "",
    val description: String,
    val imageUrl: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val regionId: String? = null,
    val cityId: String? = null,
    val category: String = "",
    val rating: Double = 0.0,
    val popularity: Int = 0,
    val priceLabel: String = ""
)
