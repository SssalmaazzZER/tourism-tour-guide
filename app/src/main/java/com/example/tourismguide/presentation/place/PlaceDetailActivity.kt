package com.example.tourismguide.presentation.place

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityPlaceDetailBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.place.adapter.PhotoGalleryAdapter
import com.example.tourismguide.presentation.place.adapter.ReviewAdapter
import com.example.tourismguide.presentation.place.adapter.ThingsToDoAdapter
import com.example.tourismguide.presentation.itinerary.SaveToItineraryBottomSheet
import com.example.tourismguide.util.ImageUtils
import com.example.tourismguide.util.PermissionHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaceDetailActivity : LocalizedActivity() {
    private val viewModel: PlaceDetailViewModel by viewModels()
    private lateinit var binding: ActivityPlaceDetailBinding
    private lateinit var photoAdapter: PhotoGalleryAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var thingsAdapter: ThingsToDoAdapter

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) launchCamera() else Snackbar.make(binding.root, R.string.camera_permission_denied, Snackbar.LENGTH_LONG).show()
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val bitmap = result.data?.extras?.get("data") as? Bitmap
        viewModel.handleCameraBitmap(bitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlaceDetailActivityArgs.fromBundle(intent.extras ?: Bundle())
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupLists()
        setupActions()
        observe()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupLists() {
        photoAdapter = PhotoGalleryAdapter()
        reviewAdapter = ReviewAdapter()
        thingsAdapter = ThingsToDoAdapter()
        binding.thingsRecycler.layoutManager = LinearLayoutManager(this)
        binding.thingsRecycler.adapter = thingsAdapter
        binding.photoRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.photoRecycler.adapter = photoAdapter
        binding.reviewRecycler.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycler.adapter = reviewAdapter
    }

    private fun setupActions() {
        binding.fabTakePhoto.setOnClickListener {
            if (PermissionHelper.checkPermission(this, Manifest.permission.CAMERA)) launchCamera() else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        binding.fabItinerary.setOnClickListener {
            SaveToItineraryBottomSheet.newInstance(viewModel.placeId).show(supportFragmentManager, SaveToItineraryBottomSheet.TAG)
        }
        binding.fabShare.setOnClickListener {
            val place = viewModel.uiState.value.place ?: return@setOnClickListener
            val text = getString(R.string.share_place_text, place.name, place.latitude, place.longitude)
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text), getString(R.string.share)))
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    state.place?.let { place ->
                        binding.collapsingToolbar.title = place.name
                        ImageUtils.loadWithCoil(binding.heroImage, place.imageUrl)
                        binding.ratingBar.rating = place.rating.toFloat()
                        binding.textRatingSummary.text = getString(R.string.rating_review_count, place.rating, state.reviews.size)
                        binding.textDistance.text = getString(R.string.distance_away, place.distanceKm)
                        binding.textCategory.text = place.category
                        binding.textWeather.text = getString(R.string.weather_stub)
                        thingsAdapter.submitList(place.description.split(".", ",").map { it.trim() }.filter { it.isNotBlank() })
                    }
                    photoAdapter.submitList(state.photos)
                    reviewAdapter.submitList(state.reviews)
                    state.error?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() }
                }
            }
        }
    }

    private fun launchCamera() {
        cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }
}
