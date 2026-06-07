package com.example.tourismguide.presentation.home

import android.content.Context
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.model.Place
import com.example.tourismguide.domain.usecase.GetNearbyPlacesUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @MockK
    lateinit var getNearbyPlacesUseCase: GetNearbyPlacesUseCase

    @MockK
    lateinit var context: Context

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testInitialState() {
        every { getNearbyPlacesUseCase(any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Loading
        )
        every { context.getString(any<Int>()) } returns "Test"

        viewModel = HomeViewModel(getNearbyPlacesUseCase, context)

        assertEquals(null, viewModel.selectedCategory.value)
        assertTrue(viewModel.uiState.value is HomeUiState.Loading)
    }

    @Test
    fun testCategorySelection() = runTest {
        every { getNearbyPlacesUseCase(any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Success(emptyList<com.example.tourismguide.domain.model.Place>())
        )
        every { context.getString(any<Int>()) } returns "Test"

        viewModel = HomeViewModel(getNearbyPlacesUseCase, context)

        viewModel.selectCategory("culture")
        assertEquals("culture", viewModel.selectedCategory.value)
    }

    @Test
    fun testLoadingState() = runTest {
        every { getNearbyPlacesUseCase(any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Loading
        )
        every { context.getString(any<Int>()) } returns "Test"

        viewModel = HomeViewModel(getNearbyPlacesUseCase, context)

        assertTrue(viewModel.uiState.value is HomeUiState.Loading)
    }

    @Test
    fun testErrorState() = runTest {
        val errorMessage = "Network error"
        every { getNearbyPlacesUseCase(any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Error(errorMessage)
        )
        every { context.getString(any<Int>()) } returns "Test"

        viewModel = HomeViewModel(getNearbyPlacesUseCase, context)

        val state = viewModel.uiState.value
        assertTrue(state is HomeUiState.Error)
        assertEquals(errorMessage, (state as HomeUiState.Error).message)
    }

    @Test
    fun testSuccessState() = runTest {
        val places = listOf(
            createTestPlace("1", "Place 1", 4.5f),
            createTestPlace("2", "Place 2", 4.8f)
        )
        every { getNearbyPlacesUseCase(any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Success(places)
        )
        every { context.getString(any<Int>()) } returns "Test"

        viewModel = HomeViewModel(getNearbyPlacesUseCase, context)

        val state = viewModel.uiState.value
        assertTrue(state is HomeUiState.Content)
        assertEquals(3, (state as HomeUiState.Content).sections.size)
    }

    private fun createTestPlace(
        id: String,
        name: String,
        rating: Float
    ) = Place(
        id = id,
        name = name,
        description = "Test description",
        category = "test",
        latitude = 33.5731,
        longitude = -7.5898,
        imageUrl = "https://example.com/image.jpg",
        rating = rating.toDouble(),
        isSaved = false,
        distanceKm = 0.0
    )
}
