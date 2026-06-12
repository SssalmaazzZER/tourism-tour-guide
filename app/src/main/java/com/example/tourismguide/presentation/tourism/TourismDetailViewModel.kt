package com.example.tourismguide.presentation.tourism

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.domain.model.ContentRichDetail
import com.example.tourismguide.domain.model.TourismContent
import com.example.tourismguide.domain.repository.ItineraryRepository
import com.example.tourismguide.domain.repository.TourismRepository
import com.example.tourismguide.data.preferences.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class TourismDetailViewModel @Inject constructor(
    private val repository: TourismRepository,
    private val itineraryRepository: ItineraryRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private var contentId: String = ""

    private val _content = MutableStateFlow<TourismContent?>(null)
    val content: StateFlow<TourismContent?> = _content.asStateFlow()

    private val _richDetail = MutableStateFlow<ContentRichDetail?>(null)
    val richDetail: StateFlow<ContentRichDetail?> = _richDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun load(contentId: String) {
        if (this.contentId == contentId && _content.value != null) return
        this.contentId = contentId
        viewModelScope.launch {
            val item = repository.getContentById(contentId) ?: return@launch
            _content.value = item
            _richDetail.value = repository.getRichDetail(contentId)
            repository.recordRecentlyViewed(item)
            _isFavorite.value = repository.isFavorite(GUEST_USER_ID, contentId)
        }
    }

    fun toggleFavorite() {
        val item = _content.value ?: return
        viewModelScope.launch {
            _isFavorite.value = repository.toggleFavorite(GUEST_USER_ID, item)
        }
    }

    fun createItineraryFromCircuit(onCreated: (String) -> Unit) {
        val item = _content.value ?: return
        if (item.contentType != "CIRCUIT") return
        viewModelScope.launch {
            val userId = dataStoreManager.userId.first().orEmpty().ifBlank { GUEST_USER_ID }
            val id = itineraryRepository.createItinerary(item.title, userId).toString()
            val circuitId = item.id.removePrefix("circuit-")
            itineraryRepository.importCircuitDays(id, circuitId)
            onCreated(id)
        }
    }

    private companion object {
        const val GUEST_USER_ID = "guest"
    }
}
