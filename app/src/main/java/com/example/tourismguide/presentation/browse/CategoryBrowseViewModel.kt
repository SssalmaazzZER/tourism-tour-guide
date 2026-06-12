package com.example.tourismguide.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.domain.model.SearchFilter
import com.example.tourismguide.domain.model.TourismContent
import com.example.tourismguide.domain.repository.TourismRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CategoryBrowseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: TourismRepository
) : ViewModel() {

    val contentType: String = savedStateHandle.get<String>("contentType") ?: "CITY"
    private val _filter = MutableStateFlow(SearchFilter(category = contentType))

    val items: StateFlow<List<TourismContent>> = _filter
        .flatMapLatest { filter ->
            when {
                contentType == "FAVORITE" -> repository.observeFavorites(GUEST_USER_ID)
                filter.regionId != null || filter.minRating > 0 -> repository.filter(filter)
                else -> repository.observeByType(contentType)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private companion object {
        const val GUEST_USER_ID = "guest"
    }

    fun applyRegionFilter(regionId: String?) {
        _filter.value = _filter.value.copy(regionId = regionId, category = contentType)
    }

    fun applyRatingFilter(minRating: Double) {
        _filter.value = _filter.value.copy(minRating = minRating, category = contentType)
    }
}
