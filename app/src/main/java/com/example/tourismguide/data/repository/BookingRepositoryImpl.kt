package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.BookingDao
import com.example.tourismguide.data.local.entity.BookingEntity
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.BookingRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

/*
Firestore rules: bookings read/write only if auth.uid matches the booking userId or guideId involved in the tour.
*/
class BookingRepositoryImpl @Inject constructor(
    private val bookingDao: BookingDao,
    private val firestore: FirebaseFirestore
) : BookingRepository {
    override fun getUserBookings(userId: String): Flow<List<BookingEntity>> = bookingDao.observeByUser(userId)
    override fun getGuideBookings(guideId: String): Flow<List<BookingEntity>> = bookingDao.observeByGuide(guideId)

    override suspend fun createBooking(booking: BookingEntity): NetworkResult<Unit> = try {
        bookingDao.upsert(booking)
        firestore.collection("bookings").document(booking.id).set(booking).await()
        NetworkResult.Success(Unit)
    } catch (exception: Exception) {
        NetworkResult.Error(exception.message ?: "Unable to create booking")
    }
}
