package com.example.tourismguide.presentation.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.usecase.SearchGuidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class GuideListViewModel @Inject constructor(
    private val searchGuidesUseCase: SearchGuidesUseCase
) : ViewModel() {
    private val _filters = MutableStateFlow(GuideFilters())
    val filters: StateFlow<GuideFilters> = _filters.asStateFlow()

    val guides: StateFlow<NetworkResult<List<GuideEntity>>> = _filters.flatMapLatest { filters ->
        searchGuidesUseCase(filters.languageQuery, filters.minRating, filters.maxPrice).map { result ->
            if (result is NetworkResult.Success && filters.query.isNotBlank()) {
                NetworkResult.Success(result.data.filter { it.name.contains(filters.query, ignoreCase = true) || it.specialities.contains(filters.query, ignoreCase = true) })
            } else {
                result
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NetworkResult.Loading)

    fun setQuery(query: String) {
        _filters.value = _filters.value.copy(query = query)
    }

    fun applyFilters(filters: GuideFilters) {
        _filters.value = filters.copy(query = _filters.value.query)
    }

    fun resetFilters() {
        _filters.value = GuideFilters(query = _filters.value.query)
    }
}
