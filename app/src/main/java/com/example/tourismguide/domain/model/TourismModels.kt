package com.example.tourismguide.domain.model

data class TourismContent(
    val id: String,
    val contentType: String,
    val title: String,
    val subtitle: String,
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

data class DiscoverCategory(
    val type: String,
    @androidx.annotation.StringRes val titleRes: Int,
    val iconRes: Int
)

data class Circuit(
    val id: String,
    val name: String,
    val description: String,
    val circuitType: String,
    val durationDays: Int,
    val imageUrl: String,
    val steps: List<CircuitStep> = emptyList()
)

data class CircuitStep(
    val id: String,
    val stepOrder: Int,
    val title: String,
    val description: String,
    val referenceType: String,
    val referenceId: String,
    val latitude: Double,
    val longitude: Double
)

data class SearchFilter(
    val regionId: String? = null,
    val category: String? = null,
    val minRating: Double = 0.0
)

enum class ContentType(val key: String) {
    CITY("CITY"),
    MONUMENT("MONUMENT"),
    CULTURE("CULTURE"),
    ARCHITECTURE("ARCHITECTURE"),
    MUSIC("MUSIC"),
    GASTRONOMY("GASTRONOMY"),
    FESTIVAL("FESTIVAL"),
    ARTISANAT("ARTISANAT"),
    NATURE("NATURE"),
    ACTIVITY("ACTIVITY"),
    MUSEUM("MUSEUM"),
    UNESCO("UNESCO"),
    EVENT("EVENT"),
    CIRCUIT("CIRCUIT")
}
