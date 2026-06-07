package com.example.tourismguide.presentation.live

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.domain.repository.GuideRepository
import com.example.tourismguide.domain.repository.GuideRequestRepository
import com.example.tourismguide.domain.usecase.TrackGuideLocationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LiveTrackingViewModelTest {

    @MockK lateinit var trackGuideLocationUseCase: TrackGuideLocationUseCase
    @MockK lateinit var guideRequestRepository: GuideRequestRepository
    @MockK lateinit var guideRepository: GuideRepository
    @MockK lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @MockK lateinit var context: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCanCancelWithin2Minutes() = runTest {
        val request = GuideRequestEntity(
            id = "request-1",
            userId = "user-1",
            guideId = "guide-1",
            status = "PENDING",
            requestedDate = 1L,
            durationHours = 2,
            peopleCount = 2,
            startLat = 33.0,
            startLng = -7.0,
            specialRequests = "",
            estimatedPrice = 400.0,
            createdAt = System.currentTimeMillis() - 60_000
        )

        every { trackGuideLocationUseCase.invoke(any()) } returns flowOf(null)
        every { guideRequestRepository.listenToRequest("request-1") } returns flowOf(request)
        every { guideRepository.getGuideById("guide-1") } returns flowOf(null)

        val viewModel = LiveTrackingViewModel(
            SavedStateHandle(mapOf("requestId" to "request-1", "guideId" to "guide-1")),
            trackGuideLocationUseCase,
            guideRequestRepository,
            guideRepository,
            fusedLocationProviderClient,
            context
        )

        assertTrue(viewModel.canCancel.value)
    }
}
