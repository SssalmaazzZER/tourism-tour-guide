package com.example.tourismguide.di

import android.content.Context
import androidx.room.Room
import com.example.tourismguide.data.local.AppDatabase
import com.example.tourismguide.data.local.dao.BookingDao
import com.example.tourismguide.data.local.dao.GuideDao
import com.example.tourismguide.data.local.dao.GuideRequestDao
import com.example.tourismguide.data.local.dao.ItineraryDao
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.dao.ReviewDao
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
}
