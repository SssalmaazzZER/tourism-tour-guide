package com.example.tourismguide.di

import android.content.Context
import androidx.room.Room
import com.example.tourismguide.data.local.AppDatabase
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "tourism_guide.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides fun providePlaceDao(db: AppDatabase): PlaceDao = db.placeDao()
    @Provides fun provideGuideDao(db: AppDatabase): GuideDao = db.guideDao()
    @Provides fun provideItineraryDao(db: AppDatabase): ItineraryDao = db.itineraryDao()
    @Provides fun provideBookingDao(db: AppDatabase): BookingDao = db.bookingDao()
    @Provides fun provideReviewDao(db: AppDatabase): ReviewDao = db.reviewDao()
    @Provides fun provideUserPhotoDao(db: AppDatabase): UserPhotoDao = db.userPhotoDao()
    @Provides fun provideGuideRequestDao(db: AppDatabase): GuideRequestDao = db.guideRequestDao()
    @Provides fun provideRegionDao(db: AppDatabase): RegionDao = db.regionDao()
    @Provides fun provideCityDao(db: AppDatabase): CityDao = db.cityDao()
    @Provides fun provideMonumentDao(db: AppDatabase): MonumentDao = db.monumentDao()
    @Provides fun provideCultureDao(db: AppDatabase): CultureDao = db.cultureDao()
    @Provides fun provideArchitectureDao(db: AppDatabase): ArchitectureDao = db.architectureDao()
    @Provides fun provideMusicDao(db: AppDatabase): MusicDao = db.musicDao()
    @Provides fun provideDishDao(db: AppDatabase): DishDao = db.dishDao()
    @Provides fun provideLocalProductDao(db: AppDatabase): LocalProductDao = db.localProductDao()
    @Provides fun provideFestivalDao(db: AppDatabase): FestivalDao = db.festivalDao()
    @Provides fun provideArtisanatDao(db: AppDatabase): ArtisanatDao = db.artisanatDao()
    @Provides fun provideNatureDao(db: AppDatabase): NatureDao = db.natureDao()
    @Provides fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()
    @Provides fun provideMuseumDao(db: AppDatabase): MuseumDao = db.museumDao()
    @Provides fun provideUnescoDao(db: AppDatabase): UnescoDao = db.unescoDao()
    @Provides fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()
    @Provides fun provideCircuitDao(db: AppDatabase): CircuitDao = db.circuitDao()
    @Provides fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
    @Provides fun provideSearchHistoryDao(db: AppDatabase): SearchHistoryDao = db.searchHistoryDao()
    @Provides fun provideRecentlyViewedDao(db: AppDatabase): RecentlyViewedDao = db.recentlyViewedDao()
    @Provides fun provideTourismOfficeDao(db: AppDatabase): TourismOfficeDao = db.tourismOfficeDao()
    @Provides fun provideRecommendationDao(db: AppDatabase): RecommendationDao = db.recommendationDao()
    @Provides fun provideContentIndexDao(db: AppDatabase): ContentIndexDao = db.contentIndexDao()
    @Provides fun provideContentRichDetailDao(db: AppDatabase): ContentRichDetailDao = db.contentRichDetailDao()
    @Provides fun provideUserItineraryDayDao(db: AppDatabase): UserItineraryDayDao = db.userItineraryDayDao()
    @Provides fun provideScheduledReminderDao(db: AppDatabase): ScheduledReminderDao = db.scheduledReminderDao()
}
