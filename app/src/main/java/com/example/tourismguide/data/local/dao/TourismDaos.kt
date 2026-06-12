package com.example.tourismguide.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tourismguide.data.local.entity.ActivityEntity
import com.example.tourismguide.data.local.entity.ArchitectureEntity
import com.example.tourismguide.data.local.entity.ArtisanatEntity
import com.example.tourismguide.data.local.entity.CircuitEntity
import com.example.tourismguide.data.local.entity.CircuitStepEntity
import com.example.tourismguide.data.local.entity.CityEntity
import com.example.tourismguide.data.local.entity.ContentIndexEntity
import com.example.tourismguide.data.local.entity.ContentRichDetailEntity
import com.example.tourismguide.data.local.entity.ScheduledReminderEntity
import com.example.tourismguide.data.local.entity.UserItineraryDayEntity
import com.example.tourismguide.data.local.entity.CultureEntity
import com.example.tourismguide.data.local.entity.DishEntity
import com.example.tourismguide.data.local.entity.EventEntity
import com.example.tourismguide.data.local.entity.FavoriteEntity
import com.example.tourismguide.data.local.entity.FestivalEntity
import com.example.tourismguide.data.local.entity.LocalProductEntity
import com.example.tourismguide.data.local.entity.MonumentEntity
import com.example.tourismguide.data.local.entity.MuseumEntity
import com.example.tourismguide.data.local.entity.MusicStyleEntity
import com.example.tourismguide.data.local.entity.NatureSiteEntity
import com.example.tourismguide.data.local.entity.RecentlyViewedEntity
import com.example.tourismguide.data.local.entity.RecommendationEntity
import com.example.tourismguide.data.local.entity.RegionEntity
import com.example.tourismguide.data.local.entity.SearchHistoryEntity
import com.example.tourismguide.data.local.entity.TourismOfficeEntity
import com.example.tourismguide.data.local.entity.UnescoHeritageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {
    @Query("SELECT * FROM regions ORDER BY name") fun observeAll(): Flow<List<RegionEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<RegionEntity>)
}

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY popularity DESC, name") fun observeAll(): Flow<List<CityEntity>>
    @Query("SELECT * FROM cities WHERE regionId = :regionId ORDER BY popularity DESC") fun observeByRegion(regionId: String): Flow<List<CityEntity>>
    @Query("SELECT * FROM cities WHERE id = :id") suspend fun getById(id: String): CityEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<CityEntity>)
}

@Dao
interface MonumentDao {
    @Query("SELECT * FROM monuments ORDER BY popularity DESC, name") fun observeAll(): Flow<List<MonumentEntity>>
    @Query("SELECT * FROM monuments WHERE cityId = :cityId") fun observeByCity(cityId: String): Flow<List<MonumentEntity>>
    @Query("SELECT * FROM monuments WHERE id = :id") suspend fun getById(id: String): MonumentEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<MonumentEntity>)
}

@Dao
interface CultureDao {
    @Query("SELECT * FROM culture_items ORDER BY title") fun observeAll(): Flow<List<CultureEntity>>
    @Query("SELECT * FROM culture_items WHERE id = :id") suspend fun getById(id: String): CultureEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<CultureEntity>)
}

@Dao
interface ArchitectureDao {
    @Query("SELECT * FROM architecture_items ORDER BY title") fun observeAll(): Flow<List<ArchitectureEntity>>
    @Query("SELECT * FROM architecture_items WHERE id = :id") suspend fun getById(id: String): ArchitectureEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<ArchitectureEntity>)
}

@Dao
interface MusicDao {
    @Query("SELECT * FROM music_styles ORDER BY name") fun observeAll(): Flow<List<MusicStyleEntity>>
    @Query("SELECT * FROM music_styles WHERE id = :id") suspend fun getById(id: String): MusicStyleEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<MusicStyleEntity>)
}

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes ORDER BY name") fun observeAll(): Flow<List<DishEntity>>
    @Query("SELECT * FROM dishes WHERE dishType = :type ORDER BY name") fun observeByType(type: String): Flow<List<DishEntity>>
    @Query("SELECT * FROM dishes WHERE id = :id") suspend fun getById(id: String): DishEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<DishEntity>)
}

@Dao
interface LocalProductDao {
    @Query("SELECT * FROM local_products ORDER BY name") fun observeAll(): Flow<List<LocalProductEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<LocalProductEntity>)
}

@Dao
interface FestivalDao {
    @Query("SELECT * FROM festivals ORDER BY name") fun observeAll(): Flow<List<FestivalEntity>>
    @Query("SELECT * FROM festivals WHERE id = :id") suspend fun getById(id: String): FestivalEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<FestivalEntity>)
}

@Dao
interface ArtisanatDao {
    @Query("SELECT * FROM artisanat_items ORDER BY name") fun observeAll(): Flow<List<ArtisanatEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<ArtisanatEntity>)
}

@Dao
interface NatureDao {
    @Query("SELECT * FROM nature_sites ORDER BY rating DESC") fun observeAll(): Flow<List<NatureSiteEntity>>
    @Query("SELECT * FROM nature_sites WHERE natureType = :type") fun observeByType(type: String): Flow<List<NatureSiteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<NatureSiteEntity>)
}

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY rating DESC") fun observeAll(): Flow<List<ActivityEntity>>
    @Query("SELECT * FROM activities WHERE activityType = :type") fun observeByType(type: String): Flow<List<ActivityEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<ActivityEntity>)
}

@Dao
interface MuseumDao {
    @Query("SELECT * FROM museums ORDER BY rating DESC") fun observeAll(): Flow<List<MuseumEntity>>
    @Query("SELECT * FROM museums WHERE id = :id") suspend fun getById(id: String): MuseumEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<MuseumEntity>)
}

@Dao
interface UnescoDao {
    @Query("SELECT * FROM unesco_heritage ORDER BY name") fun observeAll(): Flow<List<UnescoHeritageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<UnescoHeritageEntity>)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startDate") fun observeAll(): Flow<List<EventEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<EventEntity>)
}

@Dao
interface CircuitDao {
    @Query("SELECT * FROM circuits ORDER BY name") fun observeAll(): Flow<List<CircuitEntity>>
    @Query("SELECT * FROM circuits WHERE id = :id") suspend fun getById(id: String): CircuitEntity?
    @Query("SELECT * FROM circuit_steps WHERE circuitId = :circuitId ORDER BY stepOrder") fun observeSteps(circuitId: String): Flow<List<CircuitStepEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<CircuitEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSteps(items: List<CircuitStepEntity>)
    @Query("DELETE FROM circuits WHERE isPreset = 0") suspend fun deleteCustomCircuits()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY savedAt DESC") fun observeByUser(userId: String): Flow<List<FavoriteEntity>>
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND targetType = :type AND targetId = :targetId)") suspend fun isFavorite(userId: String, type: String, targetId: String): Boolean
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: FavoriteEntity)
    @Query("DELETE FROM favorites WHERE userId = :userId AND targetType = :type AND targetId = :targetId") suspend fun delete(userId: String, type: String, targetId: String)
}

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT 20") fun observeRecent(): Flow<List<SearchHistoryEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(item: SearchHistoryEntity)
    @Query("DELETE FROM search_history") suspend fun clearAll()
}

@Dao
interface RecentlyViewedDao {
    @Query("SELECT * FROM recently_viewed ORDER BY viewedAt DESC LIMIT 30") fun observeRecent(): Flow<List<RecentlyViewedEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: RecentlyViewedEntity)
    @Query("DELETE FROM recently_viewed WHERE viewedAt < :cutoff") suspend fun prune(cutoff: Long)
}

@Dao
interface TourismOfficeDao {
    @Query("SELECT * FROM tourism_offices WHERE cityId = :cityId") fun observeByCity(cityId: String): Flow<List<TourismOfficeEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<TourismOfficeEntity>)
}

@Dao
interface RecommendationDao {
    @Query("SELECT * FROM recommendations ORDER BY score DESC LIMIT :limit") fun observeTop(limit: Int): Flow<List<RecommendationEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<RecommendationEntity>)
}

@Dao
interface ContentRichDetailDao {
    @Query("SELECT * FROM content_rich_details WHERE contentId = :contentId") suspend fun getByContentId(contentId: String): ContentRichDetailEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<ContentRichDetailEntity>)
}

@Dao
interface UserItineraryDayDao {
    @Query("SELECT * FROM user_itinerary_days WHERE itineraryId = :itineraryId ORDER BY dayOrder") fun observeByItinerary(itineraryId: String): Flow<List<UserItineraryDayEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<UserItineraryDayEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: UserItineraryDayEntity)
    @Query("DELETE FROM user_itinerary_days WHERE id = :id") suspend fun deleteById(id: String)
    @Query("DELETE FROM user_itinerary_days WHERE itineraryId = :itineraryId") suspend fun deleteByItinerary(itineraryId: String)
}

@Dao
interface ScheduledReminderDao {
    @Query("SELECT * FROM scheduled_reminders WHERE isFired = 0 AND triggerAt <= :now") suspend fun dueReminders(now: Long): List<ScheduledReminderEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: ScheduledReminderEntity)
    @Query("UPDATE scheduled_reminders SET isFired = 1 WHERE id = :id") suspend fun markFired(id: String)
}

@Dao
interface ContentIndexDao {
    @Query("SELECT * FROM content_index WHERE latitude != 0 AND longitude != 0") suspend fun getAllWithLocation(): List<ContentIndexEntity>
    @Query("SELECT * FROM content_index WHERE latitude != 0 AND longitude != 0 ORDER BY popularity DESC")
    fun observeMapMarkers(): Flow<List<ContentIndexEntity>>
    @Query("SELECT * FROM content_index WHERE contentType = :type ORDER BY popularity DESC, title") fun observeByType(type: String): Flow<List<ContentIndexEntity>>
    @Query("SELECT * FROM content_index ORDER BY popularity DESC LIMIT :limit") fun observeFeatured(limit: Int): Flow<List<ContentIndexEntity>>
    @Query(
        """
        SELECT * FROM content_index
        WHERE title LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
           OR subtitle LIKE '%' || :query || '%'
        ORDER BY popularity DESC, rating DESC
        LIMIT 50
        """
    )
    fun search(query: String): Flow<List<ContentIndexEntity>>

    @Query(
        """
        SELECT * FROM content_index
        WHERE (:regionId IS NULL OR regionId = :regionId)
          AND (:category IS NULL OR category = :category OR contentType = :category)
          AND (:minRating = 0 OR rating >= :minRating)
        ORDER BY popularity DESC
        """
    )
    fun filter(regionId: String?, category: String?, minRating: Double): Flow<List<ContentIndexEntity>>

    @Query("SELECT * FROM content_index WHERE id = :id") suspend fun getById(id: String): ContentIndexEntity?
    @Query("SELECT COUNT(*) FROM content_index") suspend fun count(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<ContentIndexEntity>)
}
