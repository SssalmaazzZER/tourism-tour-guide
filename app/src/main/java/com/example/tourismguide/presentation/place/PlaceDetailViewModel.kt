package com.example.tourismguide.presentation.place

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.local.dao.UserPhotoDao
import com.example.tourismguide.data.local.entity.UserPhotoEntity
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.ItineraryRepository
import com.example.tourismguide.domain.repository.PlaceRepository
import com.example.tourismguide.domain.repository.ReviewRepository
import com.example.tourismguide.util.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository,
    private val userPhotoDao: UserPhotoDao,
    private val itineraryRepository: ItineraryRepository,
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val placeId: String = savedStateHandle["placeId"] ?: ""
    private val operationError = MutableStateFlow<String?>(null)

    val uiState: StateFlow<PlaceDetailUiState> = combine(
        placeRepository.getPlaceById(placeId),
        reviewRepository.getReviews(placeId, "PLACE"),
        userPhotoDao.observeByPlace(placeId),
        operationError
    ) { placeResult, reviews, photos, error ->
        when (placeResult) {
            NetworkResult.Loading -> PlaceDetailUiState(isLoading = true, reviews = reviews, photos = photos, error = error)
            is NetworkResult.Error -> PlaceDetailUiState(isLoading = false, reviews = reviews, photos = photos, error = placeResult.message)
            is NetworkResult.Success -> PlaceDetailUiState(isLoading = false, place = placeResult.data, reviews = reviews, photos = photos, error = error)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlaceDetailUiState())

    fun handleCameraBitmap(bitmap: Bitmap?) {
        if (bitmap == null || placeId.isBlank()) return
        viewModelScope.launch {
            runCatching {
                val compressed = ImageUtils.compressBitmap(bitmap, 1280, 1280, 82)
                val uri = saveBitmapToCache(compressed)
                val userId = dataStoreManager.userId.first() ?: "anonymous"
                val photoId = UUID.randomUUID().toString()
                userPhotoDao.upsert(
                    UserPhotoEntity(
                        id = photoId,
                        placeId = placeId,
                        userId = userId,
                        localPath = uri.toString(),
                        remoteUrl = null,
                        takenAt = System.currentTimeMillis()
                    )
                )
                ImageUtils.uploadToFirebaseStorage(uri, "places/$placeId/photos/$photoId.jpg")
                    .onSuccess { userPhotoDao.updateRemoteUrl(photoId, it) }
                    .onFailure { operationError.value = it.message }
            }.onFailure { operationError.value = it.message }
        }
    }

    fun addPlaceToItinerary(itineraryId: String) {
        viewModelScope.launch { itineraryRepository.addPlaceToItinerary(itineraryId, placeId) }
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val file = File(context.cacheDir, "place_${UUID.randomUUID()}.jpg")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
        return file.toUri()
    }
}
