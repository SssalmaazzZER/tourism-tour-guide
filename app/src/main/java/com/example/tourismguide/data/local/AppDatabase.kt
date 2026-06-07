package com.example.tourismguide.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tourismguide.data.local.dao.BookingDao
import com.example.tourismguide.data.local.dao.GuideDao
import com.example.tourismguide.data.local.dao.GuideRequestDao
import com.example.tourismguide.data.local.dao.ItineraryDao
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.dao.ReviewDao
import com.example.tourismguide.data.local.dao.UserPhotoDao
import com.example.tourismguide.data.local.entity.BookingEntity
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.data.local.entity.UserPhotoEntity

@Database(
    entities = [
        PlaceEntity::class,
        GuideEntity::class,
        ItineraryEntity::class,
        BookingEntity::class,
        ReviewEntity::class,
        UserPhotoEntity::class,
        GuideRequestEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun guideDao(): GuideDao
    abstract fun itineraryDao(): ItineraryDao
    abstract fun bookingDao(): BookingDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userPhotoDao(): UserPhotoDao
    abstract fun guideRequestDao(): GuideRequestDao
}
