package com.example.tourismguide.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tourismguide.data.local.entity.BookingEntity
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.data.local.entity.UserPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places ORDER BY rating DESC") fun observeAll(): Flow<List<PlaceEntity>>
    @Query("SELECT * FROM places WHERE category = :category ORDER BY rating DESC") fun observeByCategory(category: String): Flow<List<PlaceEntity>>
    @Query("SELECT * FROM places WHERE id = :id") fun observeById(id: String): Flow<PlaceEntity?>
    @Query("SELECT * FROM places WHERE id = :id") suspend fun getById(id: String): PlaceEntity?
    @Query("SELECT * FROM places WHERE id IN (:ids)") fun observeByIds(ids: List<String>): Flow<List<PlaceEntity>>
    @Query("SELECT * FROM places WHERE id IN (:ids)") suspend fun getPlacesByIds(ids: List<String>): List<PlaceEntity>
    @Query("SELECT * FROM places ORDER BY rating DESC") suspend fun getAll(): List<PlaceEntity>
    @Query("SELECT MAX(cachedAt) FROM places") suspend fun lastCachedAt(): Long?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(places: List<PlaceEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(place: PlaceEntity)
    @Update suspend fun update(place: PlaceEntity)
    @Delete suspend fun delete(place: PlaceEntity)
    @Query("UPDATE places SET isSaved = CASE WHEN :saved = 1 THEN 1 ELSE 0 END WHERE id = :id") suspend fun updateIsSaved(id: String, saved: Int)
    @Query("DELETE FROM places WHERE isSaved = 0") suspend fun clearCachedPlaces(): Int
    @Query("DELETE FROM places WHERE cachedAt < :cutoff AND isSaved = 0") suspend fun deleteOlderThan(cutoff: Long): Int
    @Query("SELECT * FROM places WHERE isSaved = 0 ORDER BY RANDOM() LIMIT 1") suspend fun getRandomUnsavedPlace(): PlaceEntity?
    @Query("DELETE FROM places") suspend fun deleteAllPlaces()
}

@Dao
interface GuideDao {
    @Query("SELECT * FROM guides ORDER BY rating DESC") fun observeAll(): Flow<List<GuideEntity>>
    @Query("SELECT * FROM guides ORDER BY rating DESC") suspend fun getAll(): List<GuideEntity>
    @Query("SELECT * FROM guides WHERE id = :id") fun observeById(id: String): Flow<GuideEntity?>
    @Query("SELECT * FROM guides WHERE id = :id") suspend fun getById(id: String): GuideEntity?
    @Query("SELECT * FROM guides WHERE isOnline = 1 ORDER BY rating DESC") fun observeOnline(): Flow<List<GuideEntity>>
    @Query("SELECT * FROM guides WHERE languages LIKE '%' || :language || '%' AND rating >= :minRating AND pricePerHour <= :maxPrice") fun observeFiltered(language: String, minRating: Double, maxPrice: Double): Flow<List<GuideEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(guides: List<GuideEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(guide: GuideEntity)
    @Update suspend fun update(guide: GuideEntity)
    @Delete suspend fun delete(guide: GuideEntity)
    @Query("UPDATE guides SET isOnline = :isOnline WHERE id = :id") suspend fun updateOnlineStatus(id: String, isOnline: Boolean)
}

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM itineraries WHERE userId = :userId ORDER BY startDate DESC") fun observeByUser(userId: String): Flow<List<ItineraryEntity>>
    @Query("SELECT * FROM itineraries WHERE id = :id") fun observeById(id: String): Flow<ItineraryEntity?>
    @Query("SELECT * FROM itineraries WHERE id = :id") suspend fun getById(id: String): ItineraryEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(itinerary: ItineraryEntity): Long
    @Update suspend fun update(itinerary: ItineraryEntity)
    @Query("UPDATE itineraries SET placeIds = :placeIds WHERE id = :id") suspend fun updatePlaceIds(id: String, placeIds: String)
    @Query("DELETE FROM itineraries WHERE id = :id") suspend fun deleteById(id: String)
    @Delete suspend fun delete(itinerary: ItineraryEntity)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY date DESC") fun observeByUser(userId: String): Flow<List<BookingEntity>>
    @Query("SELECT * FROM bookings WHERE guideId = :guideId ORDER BY date DESC") fun observeByGuide(guideId: String): Flow<List<BookingEntity>>
    @Query("SELECT * FROM bookings WHERE id = :id") fun observeById(id: String): Flow<BookingEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(booking: BookingEntity)
    @Update suspend fun update(booking: BookingEntity)
    @Delete suspend fun delete(booking: BookingEntity)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE targetId = :targetId AND targetType = :targetType ORDER BY createdAt DESC") fun observeForTarget(targetId: String, targetType: String): Flow<List<ReviewEntity>>
    @Query("SELECT * FROM reviews WHERE authorId = :authorId ORDER BY createdAt DESC") fun observeByAuthor(authorId: String): Flow<List<ReviewEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(review: ReviewEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(reviews: List<ReviewEntity>)
    @Update suspend fun update(review: ReviewEntity)
    @Delete suspend fun delete(review: ReviewEntity)
}

@Dao
interface UserPhotoDao {
    @Query("SELECT * FROM user_photos WHERE placeId = :placeId ORDER BY takenAt DESC") fun observeByPlace(placeId: String): Flow<List<UserPhotoEntity>>
    @Query("SELECT * FROM user_photos WHERE userId = :userId ORDER BY takenAt DESC") fun observeByUser(userId: String): Flow<List<UserPhotoEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(photo: UserPhotoEntity)
    @Query("UPDATE user_photos SET remoteUrl = :remoteUrl WHERE id = :id") suspend fun updateRemoteUrl(id: String, remoteUrl: String)
    @Update suspend fun update(photo: UserPhotoEntity)
    @Delete suspend fun delete(photo: UserPhotoEntity)
}

@Dao
interface GuideRequestDao {
    @Query("SELECT * FROM guide_requests WHERE userId = :userId ORDER BY createdAt DESC") fun observeByUser(userId: String): Flow<List<GuideRequestEntity>>
    @Query("SELECT * FROM guide_requests WHERE guideId = :guideId ORDER BY createdAt DESC") fun observeByGuide(guideId: String): Flow<List<GuideRequestEntity>>
    @Query("SELECT * FROM guide_requests WHERE guideId = :guideId AND status = 'PENDING' ORDER BY createdAt DESC") fun observePendingByGuide(guideId: String): Flow<List<GuideRequestEntity>>
    @Query("SELECT * FROM guide_requests WHERE id = :id") fun observeById(id: String): Flow<GuideRequestEntity?>
    @Query("SELECT * FROM guide_requests WHERE id = :id") suspend fun getById(id: String): GuideRequestEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(request: GuideRequestEntity)
    @Update suspend fun update(request: GuideRequestEntity)
    @Delete suspend fun delete(request: GuideRequestEntity)
    @Query("UPDATE guide_requests SET status = :status WHERE id = :id") suspend fun updateStatus(id: String, status: String)
}
