package com.example.tourismguide.presentation.itinerary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.dao.PlaceDao
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.domain.repository.ItineraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray

data class ItineraryListItem(
    val itinerary: ItineraryEntity,
    val placeCount: Int,
    val thumbnailUrl: String?
)

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val itineraryRepository: ItineraryRepository,
    private val placeDao: PlaceDao,
    private val dataStoreManager: DataStoreManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userIdFlow = dataStoreManager.userId
    private val refreshSignal = MutableStateFlow(System.currentTimeMillis())

    val itineraries: StateFlow<List<ItineraryEntity>> = combine(userIdFlow, refreshSignal) { userId, _ -> userId.orEmpty() }
        .flatMapLatest { itineraryRepository.getUserItineraries(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val itineraryItems: StateFlow<List<ItineraryListItem>> = itineraries
        .mapLatest { list ->
            list.map { itinerary ->
                val ids = itinerary.placeIds.parseIds()
                val thumbnail = ids.firstOrNull()?.let { firstId ->
                    placeDao.getPlacesByIds(listOf(firstId)).firstOrNull()?.imageUrl
                }
                ItineraryListItem(itinerary = itinerary, placeCount = ids.size, thumbnailUrl = thumbnail)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val selectedItineraryIdRaw: StateFlow<String> = savedStateHandle.getStateFlow(KEY_SELECTED_ITINERARY_ID, "")
    val selectedItineraryId: StateFlow<String?> = selectedItineraryIdRaw
        .map { it.takeIf(String::isNotBlank) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val itinerary: StateFlow<ItineraryEntity?> = selectedItineraryId
        .flatMapLatest { id ->
            if (id.isNullOrBlank()) flowOf(null) else itineraryRepository.getItineraryById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val places: StateFlow<List<PlaceEntity>> = itinerary
        .flatMapLatest { itinerary ->
            val ids = itinerary?.placeIds.parseIds()
            if (ids.isEmpty()) {
                flowOf(emptyList())
            } else {
                placeDao.observeByIds(ids).map { entities -> ids.mapNotNull { id -> entities.find { it.id == id } } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun refresh() {
        refreshSignal.value = System.currentTimeMillis()
    }

    fun startSelection(itineraryId: String) {
        savedStateHandle[KEY_SELECTED_ITINERARY_ID] = itineraryId
    }

    fun clearSelection() {
        savedStateHandle.remove<String>(KEY_SELECTED_ITINERARY_ID)
    }

    fun createItinerary(name: String) {
        viewModelScope.launch {
            val userId = userIdFlow.first().orEmpty()
            itineraryRepository.createItinerary(name, userId)
            refresh()
        }
    }

    fun createAndAdd(name: String, placeId: String) {
        viewModelScope.launch {
            val userId = userIdFlow.first().orEmpty()
            val itineraryId = itineraryRepository.createItinerary(name, userId).toString()
            itineraryRepository.addPlaceToItinerary(itineraryId, placeId)
            refresh()
        }
    }

    fun addPlaceToItinerary(itineraryId: String, placeId: String) {
        viewModelScope.launch {
            itineraryRepository.addPlaceToItinerary(itineraryId, placeId)
            refresh()
        }
    }

    fun deletePlaceFromItinerary(itineraryId: String, placeId: String) {
        viewModelScope.launch {
            itineraryRepository.deletePlaceFromItinerary(itineraryId, placeId)
            refresh()
        }
    }

    fun updatePlaceOrder(itineraryId: String, placeIds: List<String>) {
        viewModelScope.launch {
            itineraryRepository.updatePlaceOrder(itineraryId, placeIds)
            refresh()
        }
    }

    fun deleteItinerary(itinerary: ItineraryEntity) {
        viewModelScope.launch {
            itineraryRepository.deleteItinerary(itinerary.id)
            refresh()
        }
    }

    fun restoreItinerary(itinerary: ItineraryEntity) {
        viewModelScope.launch {
            itineraryRepository.restoreItinerary(itinerary)
            refresh()
        }
    }

    private fun String?.parseIds(): List<String> {
        if (this.isNullOrBlank()) return emptyList()
        val array = runCatching { JSONArray(this) }.getOrDefault(JSONArray())
        return buildList {
            for (index in 0 until array.length()) {
                val id = array.optString(index)
                if (id.isNotBlank()) add(id)
            }
        }
    }

    private companion object {
        const val KEY_SELECTED_ITINERARY_ID = "selected_itinerary_id"
    }
}
