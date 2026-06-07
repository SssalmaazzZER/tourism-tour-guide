package com.example.tourismguide.presentation.booking

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tourismguide.databinding.ActivityBookingBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingActivity : LocalizedActivity() {
    private val viewModel: BookingViewModel by viewModels()
    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        binding.buttonConfirm.setOnClickListener {
            viewModel.confirm(
                guideId = intent.getStringExtra("guideId").orEmpty(),
                placeId = intent.getStringExtra("placeId").orEmpty(),
                date = System.currentTimeMillis(),
                durationHours = 1,
                totalAmount = 0.0
            )
        }
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            is BookingEvent.NavigateToLiveTracking -> {
                                startActivity(
                                    Intent(this@BookingActivity, MainActivity::class.java)
                                        .putExtra("openLiveTracking", true)
                                        .putExtra("requestId", event.requestId)
                                        .putExtra("guideId", event.guideId)
                                )
                                finish()
                            }
                            is BookingEvent.Error -> Unit
                        }
                    }
                }
            }
        }
    }
}
