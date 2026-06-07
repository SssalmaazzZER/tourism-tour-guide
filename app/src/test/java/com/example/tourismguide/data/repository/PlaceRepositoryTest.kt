package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.remote.ApiService
import com.example.tourismguide.data.remote.dto.PlaceDto
import com.example.tourismguide.data.remote.NetworkResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue

class PlaceRepositoryTest {

    @MockK lateinit var placeDao: PlaceDao
    @MockK lateinit var apiService: ApiService

    private lateinit var repository: PlaceRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = PlaceRepositoryImpl(placeDao, apiService)
    }

    @Test
    fun testReturnsCacheWhenFreshEnough() = runBlocking {
        coEvery { placeDao.lastCachedAt() } returns System.currentTimeMillis()
        every { placeDao.observeAll() } returns flowOf(
            listOf(
                createPlace("cached-1", "Cached Place")
            )
        )

        val emissions = repository.getNearbyPlaces(0.0, 0.0, 50.0, null).take(2).toList()
        assertTrue(emissions.first() is NetworkResult.Loading)
        assertTrue(emissions.last() is NetworkResult.Success)

        coVerify(exactly = 0) { apiService.getPlaces(any(), any(), any(), any()) }
    }

    @Test
    fun testRefreshesWhenCacheStale() = runBlocking {
        coEvery { placeDao.lastCachedAt() } returns 0L
        every { placeDao.observeAll() } returns flowOf(
            listOf(
                createPlace("cached-1", "Cached Place")
            )
        )
        coEvery { placeDao.getAll() } returns emptyList()
        coEvery { apiService.getPlaces(any(), any(), any(), any()) } returns listOf(
            PlaceDto(
                id = "remote-1",
                name = "Remote Place",
                description = "Remote description",
                category = "CULTURE",
                latitude = 0.0,
                longitude = 0.0,
                imageUrl = "https://example.com/image.jpg",
                rating = 4.9
            )
        )

        val emissions = repository.getNearbyPlaces(0.0, 0.0, 50.0, null).take(2).toList()
        assertTrue(emissions.first() is NetworkResult.Loading)
        assertTrue(emissions.last() is NetworkResult.Success)

        coVerify(exactly = 1) { apiService.getPlaces(any(), any(), any(), any()) }
        coVerify(exactly = 1) { placeDao.upsertAll(any<List<PlaceEntity>>()) }
    }

    @Test
    fun testSavePlaceUpdatesIsSaved() = runBlocking {
        coEvery { placeDao.updateIsSaved("place-1", 1) } returns Unit

        repository.savePlace("place-1")

        coVerify(exactly = 1) { placeDao.updateIsSaved("place-1", 1) }
    }

    private fun createPlace(id: String, name: String) = PlaceEntity(
        id = id,
        name = name,
        description = "Description",
        category = "CULTURE",
        latitude = 33.0,
        longitude = -7.0,
        imageUrl = "https://example.com/image.jpg",
        rating = 4.5,
        reviewCount = 0,
        address = "Address",
        placeType = "attraction",
        isSaved = false,
        cachedAt = System.currentTimeMillis(),
        lastAccessedAt = 0L
    )
}
