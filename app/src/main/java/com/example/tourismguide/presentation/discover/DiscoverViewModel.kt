package com.example.tourismguide.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.domain.model.TourismContent
import com.example.tourismguide.domain.repository.TourismRepository
import com.example.tourismguide.presentation.navigation.CategoryNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    repository: TourismRepository
) : ViewModel() {

    val categories = CategoryNavigation.allCategories

    val featured: StateFlow<List<TourismContent>> = repository.observeFeatured(8)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
