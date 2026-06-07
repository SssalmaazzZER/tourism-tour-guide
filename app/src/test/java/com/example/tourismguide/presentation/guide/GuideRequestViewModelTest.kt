package com.example.tourismguide.presentation.guide

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.repository.PlaceRepository
import com.example.tourismguide.domain.usecase.SendGuideRequestUseCase
import com.example.tourismguide.util.PermissionHelper
import com.google.android.gms.location.FusedLocationProviderClient
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkObject
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GuideRequestViewModelTest {

    @MockK lateinit var sendGuideRequestUseCase: SendGuideRequestUseCase
    @MockK lateinit var guideRepository: GuideRepository
    @MockK lateinit var placeRepository: PlaceRepository
    @MockK lateinit var dataStoreManager: DataStoreManager
    @MockK lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @MockK lateinit var context: Context

    private val savedStateHandle = SavedStateHandle(mapOf("guideId" to "guide-1"))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(PermissionHelper)
        every { PermissionHelper.checkPermission(any(), any()) } returns false
        every { dataStoreManager.userId } returns flowOf("user-1")
        every { guideRepository.getGuideById("guide-1") } returns flowOf(
            GuideEntity(
                id = "guide-1",
                name = "Amina",
                avatarUrl = "",
                languages = "AR,FR,EN",
                rating = 4.8,
                pricePerHour = 200.0,
                phone = "212600000001",
                specialities = "Culture,Food",
                isVerified = true,
                isOnline = true
            )
        )
    }

    @After
    fun tearDown() {
        unmockkObject(PermissionHelper)
    }

    @Test
    fun testEstimatedPriceCalculation() = runTest {
        every { sendGuideRequestUseCase.invoke(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Success(
                GuideRequestEntity(
                    id = UUID.randomUUID().toString(),
                    userId = "user-1",
                    guideId = "guide-1",
                    status = "PENDING",
                    requestedDate = 1L,
                    durationHours = 3,
                    peopleCount = 2,
                    startLat = 0.0,
                    startLng = 0.0,
                    specialRequests = "",
                    estimatedPrice = 1200.0,
                    createdAt = System.currentTimeMillis()
                )
            )
        )

        val viewModel = GuideRequestViewModel(
            savedStateHandle,
            sendGuideRequestUseCase,
            guideRepository,
            placeRepository,
            dataStoreManager,
            fusedLocationProviderClient,
            context
        )

        viewModel.setDuration(3)
        viewModel.incrementPeople()

        assertEquals(1200.0, viewModel.estimatedPrice.value)
    }

    @Test
    fun testSendRequestSuccess() = runTest {
        val request = GuideRequestEntity(
            id = UUID.randomUUID().toString(),
            userId = "user-1",
            guideId = "guide-1",
            status = "PENDING",
            requestedDate = 1L,
            durationHours = 3,
            peopleCount = 2,
            startLat = 0.0,
            startLng = 0.0,
            specialRequests = "",
            estimatedPrice = 1200.0,
            createdAt = System.currentTimeMillis()
        )
        every { sendGuideRequestUseCase.invoke(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Loading,
            NetworkResult.Success(request)
        )

        val viewModel = GuideRequestViewModel(
            savedStateHandle,
            sendGuideRequestUseCase,
            guideRepository,
            placeRepository,
            dataStoreManager,
            fusedLocationProviderClient,
            context
        )

        viewModel.setDate(1L)
        viewModel.setTime(0L)
        viewModel.setDuration(3)
        viewModel.incrementPeople()
        viewModel.sendRequest("")
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.request)
        assertEquals(request.id, viewModel.state.value.request?.id)
    }

    @Test
    fun testSendRequestDisabledWithoutDate() = runTest {
        every { sendGuideRequestUseCase.invoke(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns flowOf(
            NetworkResult.Error("Please complete the request details")
        )

        val viewModel = GuideRequestViewModel(
            savedStateHandle,
            sendGuideRequestUseCase,
            guideRepository,
            placeRepository,
            dataStoreManager,
            fusedLocationProviderClient,
            context
        )

        viewModel.sendRequest("")
        advanceUntilIdle()

        assertTrue(viewModel.state.value.error?.isNotBlank() == true)
    }
}
