package com.example.tourismguide.presentation.tourism

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityTourismDetailBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.itinerary.CircuitBuilderActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TourismDetailActivity : LocalizedActivity() {
    private val viewModel: TourismDetailViewModel by viewModels()
    private lateinit var binding: ActivityTourismDetailBinding
    private val galleryAdapter = GalleryAdapter()
    private val leaderAdapter = LeaderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourismDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)

        val contentId = intent.getStringExtra(EXTRA_CONTENT_ID).orEmpty()
        viewModel.load(contentId)

        binding.galleryRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.galleryRecycler.adapter = galleryAdapter
        binding.leadersRecycler.layoutManager = LinearLayoutManager(this)
        binding.leadersRecycler.adapter = leaderAdapter

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }
        binding.btnShare.setOnClickListener { shareContent() }
        binding.btnMap.setOnClickListener { openMap() }
        binding.btnCustomizeCircuit.setOnClickListener {
            viewModel.createItineraryFromCircuit { itineraryId ->
                val circuitId = viewModel.content.value?.id?.removePrefix("circuit-").orEmpty()
                startActivity(
                    Intent(this, CircuitBuilderActivity::class.java)
                        .putExtra(CircuitBuilderActivity.EXTRA_ITINERARY_ID, itineraryId)
                        .putExtra(CircuitBuilderActivity.EXTRA_CIRCUIT_ID, circuitId)
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.content.collect { content ->
                        if (content == null) return@collect
                        binding.toolbar.title = content.title
                        binding.textTitle.text = content.title
                        binding.textSubtitle.text = content.subtitle
                        binding.textDescription.text = content.description
                        binding.heroImage.load(content.imageUrl) { crossfade(true) }
                        binding.textRating.isVisible = content.rating > 0
                        binding.textRating.text = getString(R.string.rating_format, content.rating)
                        binding.textPrice.isVisible = content.priceLabel.isNotBlank()
                        binding.textPrice.text = content.priceLabel
                        binding.btnMap.isVisible = content.latitude != 0.0 && content.longitude != 0.0
                        binding.btnCustomizeCircuit.isVisible = content.contentType == "CIRCUIT"
                    }
                }
                launch {
                    viewModel.richDetail.collect { rich ->
                        val hasGallery = !rich?.galleryUrls.isNullOrEmpty()
                        val hasLeaders = !rich?.leaders.isNullOrEmpty()
                        binding.galleryLabel.isVisible = hasGallery
                        binding.galleryRecycler.isVisible = hasGallery
                        binding.textRichBody.isVisible = !rich?.richBody.isNullOrBlank()
                        binding.textRichBody.text = rich?.richBody.orEmpty()
                        binding.leadersLabel.isVisible = hasLeaders
                        binding.leadersRecycler.isVisible = hasLeaders
                        rich?.galleryUrls?.let { galleryAdapter.submitList(it) }
                        rich?.leaders?.let { leaderAdapter.submitList(it) }
                    }
                }
                launch {
                    viewModel.isFavorite.collect { fav ->
                        binding.btnFavorite.text = getString(
                            if (fav) R.string.remove_from_favorites else R.string.add_to_favorites
                        )
                    }
                }
            }
        }
    }

    private fun shareContent() {
        val content = viewModel.content.value ?: return
        val text = getString(R.string.share_place_text, content.title, content.latitude, content.longitude)
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }, getString(R.string.share)))
    }

    private fun openMap() {
        val content = viewModel.content.value ?: return
        val uri = Uri.parse("geo:${content.latitude},${content.longitude}?q=${content.latitude},${content.longitude}(${content.title})")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    companion object {
        const val EXTRA_CONTENT_ID = "contentId"
    }
}
