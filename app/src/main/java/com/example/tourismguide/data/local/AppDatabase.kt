package com.example.tourismguide.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tourismguide.data.local.dao.ActivityDao
import com.example.tourismguide.data.local.dao.ArchitectureDao
import com.example.tourismguide.data.local.dao.ArtisanatDao
import com.example.tourismguide.data.local.dao.BookingDao
import com.example.tourismguide.data.local.dao.CircuitDao
import com.example.tourismguide.data.local.dao.CityDao
import com.example.tourismguide.data.local.dao.ContentIndexDao
import com.example.tourismguide.data.local.dao.ContentRichDetailDao
import com.example.tourismguide.data.local.dao.ScheduledReminderDao
import com.example.tourismguide.data.local.dao.UserItineraryDayDao
import com.example.tourismguide.data.local.dao.CultureDao
import com.example.tourismguide.data.local.dao.DishDao
import com.example.tourismguide.data.local.dao.EventDao
import com.example.tourismguide.data.local.dao.FavoriteDao
import com.example.tourismguide.data.local.dao.FestivalDao
import com.example.tourismguide.data.local.dao.GuideDao
import com.example.tourismguide.data.local.dao.GuideRequestDao
import com.example.tourismguide.data.local.dao.ItineraryDao
import com.example.tourismguide.data.local.dao.LocalProductDao
import com.example.tourismguide.data.local.dao.MonumentDao
import com.example.tourismguide.data.local.dao.MuseumDao
import com.example.tourismguide.data.local.dao.MusicDao
import com.example.tourismguide.data.local.dao.NatureDao
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.dao.RecentlyViewedDao
import com.example.tourismguide.data.local.dao.RecommendationDao
import com.example.tourismguide.data.local.dao.RegionDao
import com.example.tourismguide.data.local.dao.ReviewDao
import com.example.tourismguide.data.local.dao.SearchHistoryDao
import com.example.tourismguide.data.local.dao.TourismOfficeDao
import com.example.tourismguide.data.local.dao.UnescoDao
import com.example.tourismguide.data.local.dao.UserPhotoDao
import com.example.tourismguide.data.local.entity.ActivityEntity
import com.example.tourismguide.data.local.entity.ArchitectureEntity
import com.example.tourismguide.data.local.entity.ArtisanatEntity
import com.example.tourismguide.data.local.entity.BookingEntity
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
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.local.entity.IngredientEntity
import com.example.tourismguide.data.local.entity.InstrumentEntity
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.LocalProductEntity
import com.example.tourismguide.data.local.entity.MonumentEntity
import com.example.tourismguide.data.local.entity.MuseumEntity
import com.example.tourismguide.data.local.entity.MusicStyleEntity
import com.example.tourismguide.data.local.entity.NatureSiteEntity
import com.example.tourismguide.data.local.entity.NotificationEntity
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.local.entity.PointOfInterestEntity
import com.example.tourismguide.data.local.entity.RecentlyViewedEntity
import com.example.tourismguide.data.local.entity.RecommendationEntity
import com.example.tourismguide.data.local.entity.RegionEntity
import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.data.local.entity.SearchHistoryEntity
import com.example.tourismguide.data.local.entity.TourismOfficeEntity
import com.example.tourismguide.data.local.entity.UnescoHeritageEntity
import com.example.tourismguide.data.local.entity.UserEntity
import com.example.tourismguide.data.local.entity.UserPhotoEntity

@Database(
    entities = [
        PlaceEntity::class,
        GuideEntity::class,
        ItineraryEntity::class,
        BookingEntity::class,
        ReviewEntity::class,
        UserPhotoEntity::class,
        GuideRequestEntity::class,
        UserEntity::class,
        RegionEntity::class,
        CityEntity::class,
        MonumentEntity::class,
        PointOfInterestEntity::class,
        CultureEntity::class,
        ArchitectureEntity::class,
        MusicStyleEntity::class,
        InstrumentEntity::class,
        DishEntity::class,
        IngredientEntity::class,
        LocalProductEntity::class,
        FestivalEntity::class,
        ArtisanatEntity::class,
        NatureSiteEntity::class,
        ActivityEntity::class,
        MuseumEntity::class,
        UnescoHeritageEntity::class,
        EventEntity::class,
        CircuitEntity::class,
        CircuitStepEntity::class,
        FavoriteEntity::class,
        SearchHistoryEntity::class,
        RecentlyViewedEntity::class,
        NotificationEntity::class,
        TourismOfficeEntity::class,
        RecommendationEntity::class,
        ContentIndexEntity::class,
        ContentRichDetailEntity::class,
        UserItineraryDayEntity::class,
        ScheduledReminderEntity::class
    ],
    version = 4,
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
    abstract fun regionDao(): RegionDao
    abstract fun cityDao(): CityDao
    abstract fun monumentDao(): MonumentDao
    abstract fun cultureDao(): CultureDao
    abstract fun architectureDao(): ArchitectureDao
    abstract fun musicDao(): MusicDao
    abstract fun dishDao(): DishDao
    abstract fun localProductDao(): LocalProductDao
    abstract fun festivalDao(): FestivalDao
    abstract fun artisanatDao(): ArtisanatDao
    abstract fun natureDao(): NatureDao
    abstract fun activityDao(): ActivityDao
    abstract fun museumDao(): MuseumDao
    abstract fun unescoDao(): UnescoDao
    abstract fun eventDao(): EventDao
    abstract fun circuitDao(): CircuitDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun tourismOfficeDao(): TourismOfficeDao
    abstract fun recommendationDao(): RecommendationDao
    abstract fun contentIndexDao(): ContentIndexDao
    abstract fun contentRichDetailDao(): ContentRichDetailDao
    abstract fun userItineraryDayDao(): UserItineraryDayDao
    abstract fun scheduledReminderDao(): ScheduledReminderDao
}
