package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.mapper.toDomain
import com.example.tourismguide.data.mapper.toEntity
import com.example.tourismguide.data.remote.ApiService
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.model.Place
import com.example.tourismguide.domain.repository.PlaceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao,
    private val apiService: ApiService
) : PlaceRepository {
    override fun getNearbyPlaces(lat: Double, lng: Double, radius: Double, category: String?): Flow<NetworkResult<List<Place>>> = flow {
        emit(NetworkResult.Loading)
        refreshIfStale(lat, lng, radius, category)
        val source = if (category.isNullOrBlank()) placeDao.observeAll() else placeDao.observeByCategory(category)
        emitAll(source.map { NetworkResult.Success(it.map { entity -> entity.toDomain() }) })
    }.catch { emit(NetworkResult.Error(it.message ?: "Unable to load places")) }

    override fun getPlaceById(id: String): Flow<NetworkResult<Place>> = flow {
        emit(NetworkResult.Loading)
        placeDao.getById(id) ?: runCatching { apiService.getPlace(id).toEntity() }
            .onSuccess { placeDao.upsert(it) }
            .onFailure { Timber.w(it) }
        emitAll(placeDao.observeById(id).map { entity ->
            entity?.let { NetworkResult.Success(it.toDomain()) } ?: NetworkResult.Error("Place not found", 404)
        })
    }.catch { emit(NetworkResult.Error(it.message ?: "Unable to load place")) }

    override suspend fun savePlace(id: String): NetworkResult<Unit> = runCatching {
        placeDao.updateIsSaved(id, 1)
    }.fold({ NetworkResult.Success(Unit) }, { NetworkResult.Error(it.message ?: "Unable to save place") })

    override suspend fun unsavePlace(id: String): NetworkResult<Unit> = runCatching {
        placeDao.updateIsSaved(id, 0)
    }.fold({ NetworkResult.Success(Unit) }, { NetworkResult.Error(it.message ?: "Unable to unsave place") })

    override suspend fun getPopularPlaces(): List<Place> {
        return placeDao.getAll().filter { it.rating >= 4.5 }.map { it.toDomain() }
    }

    override suspend fun getAllPlaces(): List<Place> {
        return placeDao.getAll().map { it.toDomain() }
    }

    private suspend fun refreshIfStale(lat: Double, lng: Double, radius: Double, category: String?) {
        val lastCached = placeDao.lastCachedAt() ?: 0L
        if (System.currentTimeMillis() - lastCached <= CACHE_TTL_MS && placeDao.observeAll().first().isNotEmpty()) return
        
        // Load Moroccan Fallback data immediately if database is empty
        if (placeDao.getAll().isEmpty()) {
            placeDao.upsertAll(fallbackPlaces())
        }

        runCatching { apiService.getPlaces(category, lat, lng, radius).map { it.toEntity() } }
            .onSuccess { if (it.isNotEmpty()) placeDao.upsertAll(it) }
            .onFailure { Timber.w(it) }
    }

    private fun fallbackPlaces() = listOf(
        PlaceEntity(
            id = "hassan-ii",
            name = "Hassan II Mosque",
            description = "The largest mosque in Morocco and the 7th largest in the world.",
            category = "CULTURE",
            latitude = 33.6085,
            longitude = -7.6328,
            imageUrl = "https://images.unsplash.com/photo-1548018560-c7196548e84d",
            rating = 4.9,
            address = "Casablanca, Morocco",
            placeType = "landmark",
            isSaved = false,
            cachedAt = System.currentTimeMillis()
        ),
        PlaceEntity(
            id = "chefchaouen",
            name = "Blue City (Chefchaouen)",
            description = "Beautiful blue-washed buildings in the Rif Mountains.",
            category = "CITY",
            latitude = 35.1714,
            longitude = -5.2697,
            imageUrl = "https://images.unsplash.com/photo-1536728033382-9963497c2a99",
            rating = 4.8,
            address = "Chefchaouen, Morocco",
            placeType = "city",
            isSaved = false,
            cachedAt = System.currentTimeMillis()
        ),
        PlaceEntity(
            id = "merzouga",
            name = "Merzouga Saharan Dunes",
            description = "Entry point to the Erg Chebbi dunes for camel trekking and desert camping.",
            category = "ACTIVITY",
            latitude = 31.0802,
            longitude = -4.0141,
            imageUrl = "https://images.unsplash.com/photo-1489493173502-14ec3a0c5bb2",
            rating = 4.9,
            address = "Merzouga, Desert",
            placeType = "nature",
            isSaved = false,
            cachedAt = System.currentTimeMillis()
        ),
        PlaceEntity(
            id = "majorelle",
            name = "Jardin Majorelle",
            description = "YSL's iconic garden featuring cobalt blue buildings and exotic plants.",
            category = "CULTURE",
            latitude = 31.6417,
            longitude = -8.0033,
            imageUrl = "https://images.unsplash.com/photo-1510414842594-a61c69b5ae57",
            rating = 4.7,
            address = "Marrakech",
            placeType = "garden",
            isSaved = false,
            cachedAt = System.currentTimeMillis()
        ),
        PlaceEntity(
            id = "taghazout",
            name = "Taghazout Beach",
            description = "World-class surfing and laid-back beach vibes on the Atlantic.",
            category = "BEACH",
            latitude = 30.5450,
            longitude = -9.7080,
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206",
            rating = 4.6,
            address = "Agadir Region",
            placeType = "beach",
            isSaved = false,
            cachedAt = System.currentTimeMillis()
        )
    )

    private companion object {
        const val CACHE_TTL_MS = 60 * 60 * 1000L
    }
}
