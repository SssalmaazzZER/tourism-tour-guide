package com.example.tourismguide.presentation.home

import com.example.tourismguide.domain.model.Place

data class HomeSection(val title: String, val places: List<Place>)

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Content(val sections: List<HomeSection>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
