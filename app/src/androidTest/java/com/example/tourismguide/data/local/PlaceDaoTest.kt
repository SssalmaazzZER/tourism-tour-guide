package com.example.tourismguide.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tourismguide.data.local.entity.PlaceEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class PlaceDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var placeDao: PlaceDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        placeDao = database.placeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrievePlace() = runBlocking {
        val place = createTestPlace("1", "Test Place", 4.5f)
        placeDao.upsert(place)

        val retrieved = placeDao.getById("1")
        assertNotNull(retrieved)
        assertEquals("Test Place", retrieved.name)
        assertEquals(4.5, retrieved.rating)
    }

    @Test
    fun testInsertMultiplePlaces() = runBlocking {
        val places = listOf(
            createTestPlace("1", "Place 1", 4.5f),
            createTestPlace("2", "Place 2", 4.8f),
            createTestPlace("3", "Place 3", 4.2f)
        )
        placeDao.upsertAll(places)

        val all = placeDao.getAll()
        assertEquals(3, all.size)
    }

    @Test
    fun testUpdatePlace() = runBlocking {
        val place = createTestPlace("1", "Test Place", 4.5f)
        placeDao.upsert(place)

        val updated = place.copy(rating = 5.0f)
        placeDao.update(updated)

        val retrieved = placeDao.getById("1")
        assertNotNull(retrieved)
        assertEquals(5.0, retrieved.rating)
    }

    @Test
    fun testDeletePlace() = runBlocking {
        val place = createTestPlace("1", "Test Place", 4.5f)
        placeDao.upsert(place)

        placeDao.delete(place)

        val retrieved = placeDao.getById("1")
        assertTrue(retrieved == null)
    }

    @Test
    fun testGetPlacesByIds() = runBlocking {
        val places = listOf(
            createTestPlace("1", "Place 1", 4.5f),
            createTestPlace("2", "Place 2", 4.8f),
            createTestPlace("3", "Place 3", 4.2f)
        )
        placeDao.upsertAll(places)

        val retrieved = placeDao.getPlacesByIds(listOf("1", "3"))
        assertEquals(2, retrieved.size)
    }

    @Test
    fun testUpdateIsSaved() = runBlocking {
        val place = createTestPlace("1", "Test Place", 4.5f)
        placeDao.upsert(place)

        placeDao.updateIsSaved("1", 1)

        val retrieved = placeDao.getById("1")
        assertNotNull(retrieved)
        assertTrue(retrieved.isSaved)
    }

    private fun createTestPlace(
        id: String,
        name: String,
        rating: Float
    ) = PlaceEntity(
        id = id,
        name = name,
        description = "Test description",
        latitude = 33.5731,
        longitude = -7.5898,
        imageUrl = "https://example.com/image.jpg",
        rating = rating.toDouble(),
        reviewCount = 10,
        category = "test",
        address = "Test address",
        placeType = "attraction",
        isSaved = false,
        cachedAt = System.currentTimeMillis(),
        lastAccessedAt = 0L
    )
}
