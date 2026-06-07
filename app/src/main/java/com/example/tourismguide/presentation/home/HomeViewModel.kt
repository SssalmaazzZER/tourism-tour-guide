package com.example.tourismguide.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.example.tourismguide.R
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.usecase.GetNearbyPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    val uiState: StateFlow<HomeUiState> = _selectedCategory
        .flatMapLatest { category -> getNearbyPlacesUseCase(DEFAULT_LAT, DEFAULT_LNG, DEFAULT_RADIUS_KM, category) }
        .map { result ->
            when (result) {
                NetworkResult.Loading -> HomeUiState.Loading
                is NetworkResult.Error -> HomeUiState.Error(result.message)
                is NetworkResult.Success -> {
                    val places = result.data
                    HomeUiState.Content(
                        listOf(
                            HomeSection(context.getString(R.string.section_trending), places.sortedByDescending { it.rating }),
                            HomeSection(context.getString(R.string.section_nearby), places),
                            HomeSection(context.getString(R.string.section_recent), places.take(5))
                        )
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    private companion object {
        const val DEFAULT_LAT = 33.5731
        const val DEFAULT_LNG = -7.5898
        const val DEFAULT_RADIUS_KM = 50.0
    }
}
