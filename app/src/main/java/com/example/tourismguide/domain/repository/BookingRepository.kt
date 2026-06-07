package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.local.entity.BookingEntity
import com.example.tourismguide.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getUserBookings(userId: String): Flow<List<BookingEntity>>
    fun getGuideBookings(guideId: String): Flow<List<BookingEntity>>
    suspend fun createBooking(booking: BookingEntity): NetworkResult<Unit>
}
