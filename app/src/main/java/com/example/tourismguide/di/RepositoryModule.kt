package com.example.tourismguide.di

import com.example.tourismguide.data.repository.AuthRepositoryImpl
import com.example.tourismguide.data.repository.BookingRepositoryImpl
import com.example.tourismguide.data.repository.ChatRepositoryImpl
import com.example.tourismguide.data.repository.GuideRepositoryImpl
import com.example.tourismguide.data.repository.GuideRequestRepositoryImpl
import com.example.tourismguide.data.repository.ItineraryRepositoryImpl
import com.example.tourismguide.data.repository.LiveLocationRepositoryImpl
import com.example.tourismguide.data.repository.PlaceRepositoryImpl
import com.example.tourismguide.data.repository.ReviewRepositoryImpl
import com.example.tourismguide.domain.repository.AuthRepository
import com.example.tourismguide.domain.repository.BookingRepository
import com.example.tourismguide.domain.repository.ChatRepository
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.repository.ItineraryRepository
import com.example.tourismguide.domain.repository.LiveLocationRepository
import com.example.tourismguide.domain.repository.PlaceRepository
import com.example.tourismguide.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindPlaceRepository(impl: PlaceRepositoryImpl): PlaceRepository
    @Binds abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds abstract fun bindItineraryRepository(impl: ItineraryRepositoryImpl): ItineraryRepository
    @Binds abstract fun bindReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository
    @Binds abstract fun bindGuideRepository(impl: GuideRepositoryImpl): GuideRepository
    @Binds abstract fun bindGuideRequestRepository(impl: GuideRequestRepositoryImpl): GuideRequestRepository
    @Binds abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
    @Binds abstract fun bindLiveLocationRepository(impl: LiveLocationRepositoryImpl): LiveLocationRepository
    @Binds abstract fun bindBookingRepository(impl: BookingRepositoryImpl): BookingRepository
}
