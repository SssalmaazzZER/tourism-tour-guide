package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.MoroccoDataSeeder
import com.example.tourismguide.data.local.dao.CircuitDao
import com.example.tourismguide.data.local.dao.ContentIndexDao
import com.example.tourismguide.data.local.dao.ContentRichDetailDao
import com.example.tourismguide.data.local.dao.FavoriteDao
import com.example.tourismguide.data.local.entity.ScheduledReminderEntity
import com.example.tourismguide.service.ReminderScheduler
import com.example.tourismguide.data.local.dao.RecentlyViewedDao
import com.example.tourismguide.data.local.dao.SearchHistoryDao
import com.example.tourismguide.data.local.entity.FavoriteEntity
import com.example.tourismguide.data.local.entity.RecentlyViewedEntity
import com.example.tourismguide.data.local.entity.SearchHistoryEntity
import com.example.tourismguide.data.mapper.newFavoriteId
import com.example.tourismguide.data.mapper.toDomain
import com.example.tourismguide.domain.model.Circuit
import com.example.tourismguide.domain.model.ContentRichDetail
import com.example.tourismguide.domain.model.SearchFilter
import com.example.tourismguide.domain.model.TourismContent
import com.example.tourismguide.domain.repository.TourismRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TourismRepositoryImpl @Inject constructor(
    private val contentIndexDao: ContentIndexDao,
    private val contentRichDetailDao: ContentRichDetailDao,
    private val circuitDao: CircuitDao,
    private val favoriteDao: FavoriteDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val recentlyViewedDao: RecentlyViewedDao,
    private val seeder: MoroccoDataSeeder,
    private val reminderScheduler: ReminderScheduler
) : TourismRepository {

    override fun observeFeatured(limit: Int): Flow<List<TourismContent>> =
        contentIndexDao.observeFeatured(limit).map { list -> list.map { it.toDomain() } }

    override fun observeByType(type: String): Flow<List<TourismContent>> =
        contentIndexDao.observeByType(type).map { list -> list.map { it.toDomain() } }

    override fun search(query: String): Flow<List<TourismContent>> =
        contentIndexDao.search(query.trim()).map { list -> list.map { it.toDomain() } }

    override fun filter(filter: SearchFilter): Flow<List<TourismContent>> =
        contentIndexDao.filter(filter.regionId, filter.category, filter.minRating)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getContentById(id: String): TourismContent? =
        contentIndexDao.getById(id)?.toDomain()

    override suspend fun getRichDetail(contentId: String): ContentRichDetail? =
        contentRichDetailDao.getByContentId(contentId)?.toDomain()

    override fun observeMapContent(): Flow<List<TourismContent>> =
        contentIndexDao.observeMapMarkers().map { list -> list.map { it.toDomain() } }

    override suspend fun scheduleFestivalReminder(content: TourismContent) {
        if (content.contentType != "FESTIVAL") return
        val triggerAt = System.currentTimeMillis() + FESTIVAL_REMINDER_LEAD_MS
        reminderScheduler.scheduleFestivalReminder(content.id, content.title, triggerAt)
    }

    override suspend fun getUnescoSitesForGeofencing(): List<TourismContent> =
        contentIndexDao.observeByType("UNESCO").first().map { it.toDomain() }

    override fun observeCircuits(): Flow<List<Circuit>> =
        circuitDao.observeAll().map { circuits ->
            circuits.map { circuit ->
                val steps = circuitDao.observeSteps(circuit.id).first()
                circuit.toDomain(steps)
            }
        }

    override suspend fun getCircuit(id: String): Circuit? {
        val circuit = circuitDao.getById(id) ?: return null
        val steps = circuitDao.observeSteps(id).first()
        return circuit.toDomain(steps)
    }

    override suspend fun recordSearch(query: String) {
        if (query.isBlank()) return
        searchHistoryDao.insert(
            SearchHistoryEntity(
                id = System.currentTimeMillis().toString(),
                query = query.trim()
            )
        )
    }

    override fun observeSearchHistory(): Flow<List<String>> =
        searchHistoryDao.observeRecent().map { list -> list.map { it.query } }

    override suspend fun recordRecentlyViewed(content: TourismContent) {
        recentlyViewedDao.upsert(
            RecentlyViewedEntity(
                id = content.id,
                targetType = content.contentType,
                targetId = content.id,
                title = content.title,
                imageUrl = content.imageUrl
            )
        )
        recentlyViewedDao.prune(System.currentTimeMillis() - RECENT_TTL_MS)
    }

    override fun observeRecentlyViewed(): Flow<List<TourismContent>> =
        recentlyViewedDao.observeRecent().map { list ->
            list.map { rv ->
                TourismContent(
                    id = rv.targetId,
                    contentType = rv.targetType,
                    title = rv.title,
                    subtitle = "",
                    description = "",
                    imageUrl = rv.imageUrl
                )
            }
        }

    override suspend fun toggleFavorite(userId: String, content: TourismContent): Boolean {
        val exists = favoriteDao.isFavorite(userId, content.contentType, content.id)
        return if (exists) {
            favoriteDao.delete(userId, content.contentType, content.id)
            false
        } else {
            favoriteDao.upsert(
                FavoriteEntity(
                    id = newFavoriteId(),
                    userId = userId,
                    targetType = content.contentType,
                    targetId = content.id
                )
            )
            if (content.contentType == "FESTIVAL") scheduleFestivalReminder(content)
            true
        }
    }

    override fun observeFavorites(userId: String): Flow<List<TourismContent>> =
        favoriteDao.observeByUser(userId).flatMapLatest { favorites ->
            flow {
                val items = favorites.mapNotNull { fav ->
                    contentIndexDao.getById(fav.targetId)?.toDomain()
                }
                emit(items)
            }
        }

    override suspend fun isFavorite(userId: String, contentId: String): Boolean {
        val content = contentIndexDao.getById(contentId) ?: return false
        return favoriteDao.isFavorite(userId, content.contentType, contentId)
    }

    override suspend fun seedDataIfNeeded() {
        seeder.seedIfEmpty()
    }

    private companion object {
        const val RECENT_TTL_MS = 30L * 24 * 60 * 60 * 1000
        const val FESTIVAL_REMINDER_LEAD_MS = 24L * 60 * 60 * 1000
    }
}
