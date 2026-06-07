package com.example.tourismguide.presentation.guide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityGuideDetailBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.place.adapter.ReviewAdapter
import com.example.tourismguide.util.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GuideDetailActivity : LocalizedActivity() {
    private val viewModel: GuideDetailViewModel by viewModels()
    private lateinit var binding: ActivityGuideDetailBinding
    private val reviewAdapter = ReviewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.reviewRecycler.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycler.adapter = reviewAdapter
        observe()
    }

    override fun onSupportNavigateUp(): Boolean = true.also { finish() }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val guide = state.guide ?: return@collect
                    binding.collapsingToolbar.title = guide.name
                    ImageUtils.loadWithCoil(binding.imageAvatar, guide.avatarUrl)
                    binding.textName.text = guide.name
                    binding.textBio.text = guide.specialities
                    binding.textLanguages.text = guide.languages
                    binding.textSpecialities.text = guide.specialities
                    binding.ratingBar.rating = guide.rating.toFloat()
                    binding.textReviewCount.text = getString(R.string.rating_review_count, guide.rating, state.reviews.size)
                    binding.buttonHire.setOnClickListener { GuideRequestBottomSheet.newInstance(guide.id).show(supportFragmentManager, GuideRequestBottomSheet.TAG) }
                    binding.buttonCall.setOnClickListener { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${guide.phone}"))) }
                    binding.buttonWhatsapp.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${guide.phone}"))) }
                    binding.buttonShare.setOnClickListener {
                        val text = getString(R.string.share_guide_text, guide.name, guide.rating, guide.pricePerHour)
                        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text), getString(R.string.share)))
                    }
                    reviewAdapter.submitList(state.reviews)
                }
            }
        }
    }
}
