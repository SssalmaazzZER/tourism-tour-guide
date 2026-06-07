package com.example.tourismguide.data.remote

import com.example.tourismguide.data.remote.dto.BookingDto
import com.example.tourismguide.data.remote.dto.GuideDto
import com.example.tourismguide.data.remote.dto.PlaceDto
import com.example.tourismguide.data.remote.dto.ReviewDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("places")
    suspend fun getPlaces(
        @Query("category") category: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null,
        @Query("radius") radius: Double? = null
    ): List<PlaceDto>

    @GET("places/{id}")
    suspend fun getPlace(@Path("id") id: String): PlaceDto

    @GET("guides")
    suspend fun getGuides(
        @Query("language") language: String? = null,
        @Query("minRating") minRating: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null
    ): List<GuideDto>

    @GET("guides/{id}")
    suspend fun getGuide(@Path("id") id: String): GuideDto

    @GET("reviews")
    suspend fun getReviews(
        @Query("targetId") targetId: String,
        @Query("targetType") targetType: String
    ): List<ReviewDto>

    @POST("reviews")
    suspend fun postReview(@Body review: ReviewDto): ReviewDto

    @POST("bookings")
    suspend fun postBooking(@Body booking: BookingDto): BookingDto
}
