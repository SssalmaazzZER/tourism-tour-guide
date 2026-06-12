package com.example.tourismguide.domain.repository

import com.example.tourismguide.domain.model.Circuit
import com.example.tourismguide.domain.model.ContentRichDetail
import com.example.tourismguide.domain.model.SearchFilter
import com.example.tourismguide.domain.model.TourismContent
import kotlinx.coroutines.flow.Flow

interface TourismRepository {
    fun observeFeatured(limit: Int = 12): Flow<List<TourismContent>>
    fun observeByType(type: String): Flow<List<TourismContent>>
    fun search(query: String): Flow<List<TourismContent>>
    fun filter(filter: SearchFilter): Flow<List<TourismContent>>
    suspend fun getContentById(id: String): TourismContent?
    suspend fun getRichDetail(contentId: String): ContentRichDetail?
    fun observeMapContent(): Flow<List<TourismContent>>
    suspend fun scheduleFestivalReminder(content: TourismContent)
    suspend fun getUnescoSitesForGeofencing(): List<TourismContent>
    fun observeCircuits(): Flow<List<Circuit>>
    suspend fun getCircuit(id: String): Circuit?
    suspend fun recordSearch(query: String)
    fun observeSearchHistory(): Flow<List<String>>
    suspend fun recordRecentlyViewed(content: TourismContent)
    fun observeRecentlyViewed(): Flow<List<TourismContent>>
    suspend fun toggleFavorite(userId: String, content: TourismContent): Boolean
    fun observeFavorites(userId: String): Flow<List<TourismContent>>
    suspend fun isFavorite(userId: String, contentId: String): Boolean
    suspend fun seedDataIfNeeded()
}
