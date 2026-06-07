package com.example.tourismguide.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.PlaceEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndQueryPlace() = runBlocking {
        val place = place("place-1")
        database.placeDao().upsert(place)

        val result = database.placeDao().getById("place-1")
        assertNotNull(result)
        assertEquals(place.name, result.name)
    }

    @Test
    fun testInsertGuideRequest() = runBlocking {
        val request = GuideRequestEntity(
            id = "request-1",
            userId = "user-1",
            guideId = "guide-1",
            status = "PENDING",
            requestedDate = 1L,
            durationHours = 2,
            peopleCount = 2,
            startLat = 33.0,
            startLng = -7.0,
            specialRequests = "No peanuts",
            estimatedPrice = 400.0,
            createdAt = 1L
        )
        database.guideRequestDao().upsert(request)

        val result = database.guideRequestDao().getById("request-1")
        assertNotNull(result)
        assertEquals("PENDING", result.status)
        assertEquals(2, result.durationHours)
    }

    @Test
    fun testDeleteItineraryRemovesRow() = runBlocking {
        val itinerary = ItineraryEntity(
            id = "itinerary-1",
            userId = "user-1",
            name = "Weekend Trip",
            placeIds = "[]",
            startDate = 1L,
            createdAt = 1L
        )
        database.itineraryDao().upsert(itinerary)
        database.itineraryDao().deleteById("itinerary-1")

        val result = database.itineraryDao().getById("itinerary-1")
        assertNull(result)
    }

    private fun place(id: String) = PlaceEntity(
        id = id,
        name = "Test Place",
        description = "Description",
        category = "CITY",
        latitude = 33.0,
        longitude = -7.0,
        imageUrl = "https://example.com/image.jpg",
        rating = 4.5,
        reviewCount = 3,
        address = "Address",
        placeType = "attraction",
        isSaved = false,
        cachedAt = System.currentTimeMillis(),
        lastAccessedAt = 0L
    )
}
