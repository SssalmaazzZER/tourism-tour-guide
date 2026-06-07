package com.example.tourismguide.presentation.guide

import java.io.Serializable

data class GuideFilters(
    val languages: Set<String> = emptySet(),
    val minRating: Double = 1.0,
    val maxPrice: Double = 500.0,
    val query: String = ""
) : Serializable {
    val languageQuery: String? get() = languages.firstOrNull()
}
