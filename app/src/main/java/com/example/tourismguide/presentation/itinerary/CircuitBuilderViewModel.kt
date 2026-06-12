package com.example.tourismguide.presentation.itinerary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.UserItineraryDayEntity
import com.example.tourismguide.domain.repository.ItineraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CircuitBuilderViewModel @Inject constructor(
    private val itineraryRepository: ItineraryRepository
) : ViewModel() {

    private val itineraryIdFlow = MutableStateFlow("")
    private val circuitIdFlow = MutableStateFlow<String?>(null)

    val days: StateFlow<List<UserItineraryDayEntity>> = itineraryIdFlow
        .flatMapLatest { id ->
            if (id.isBlank()) kotlinx.coroutines.flow.flowOf(emptyList())
            else itineraryRepository.observeItineraryDays(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun init(itineraryId: String, circuitId: String?) {
        if (itineraryIdFlow.value == itineraryId) return
        itineraryIdFlow.value = itineraryId
        circuitIdFlow.value = circuitId
        if (!circuitId.isNullOrBlank()) {
            viewModelScope.launch { itineraryRepository.importCircuitDays(itineraryId, circuitId) }
        }
    }

    fun reorderDays(updated: List<UserItineraryDayEntity>) {
        val id = itineraryIdFlow.value
        if (id.isBlank()) return
        viewModelScope.launch { itineraryRepository.updateDayOrder(updated) }
    }

    fun deleteDay(dayId: String) {
        viewModelScope.launch { itineraryRepository.deleteDay(dayId) }
    }

    fun addDay(title: String) {
        val id = itineraryIdFlow.value
        if (id.isBlank()) return
        viewModelScope.launch { itineraryRepository.addEmptyDay(id, title) }
    }
}
