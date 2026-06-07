package com.example.tourismguide.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavOptions
import androidx.navigation.ui.setupWithNavController
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityMainBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.itinerary.ItineraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels

@AndroidEntryPoint
class MainActivity : LocalizedActivity() {
    private lateinit var binding: ActivityMainBinding
    private val itineraryViewModel: ItineraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHost.navController)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val extras = intent?.extras ?: return
        extras.getString("selectItineraryId")?.takeIf { it.isNotBlank() }?.let {
            itineraryViewModel.startSelection(it)
            binding.bottomNavigation.selectedItemId = R.id.homeFragment
        }
        if (!extras.getBoolean("openLiveTracking", false)) return
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHost.navController.navigate(
            R.id.liveTrackingFragment,
            bundleOf(
                "requestId" to extras.getString("requestId").orEmpty(),
                "guideId" to extras.getString("guideId").orEmpty()
            ),
            NavOptions.Builder().setLaunchSingleTop(true).build()
        )
    }
}
