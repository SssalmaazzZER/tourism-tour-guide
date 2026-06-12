package com.example.tourismguide.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityMainDrawerBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.itinerary.ItineraryViewModel
import com.example.tourismguide.presentation.navigation.CategoryNavigation
import com.example.tourismguide.presentation.profile.SettingsActivity
import com.example.tourismguide.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LocalizedActivity() {
    private lateinit var binding: ActivityMainDrawerBinding
    private val itineraryViewModel: ItineraryViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.guideListFragment,
                R.id.mapFragment,
                R.id.itineraryFragment,
                R.id.profileFragment,
                R.id.categoryBrowseFragment
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_guides -> navController.navigate(R.id.guideListFragment)
                else -> CategoryNavigation.routeForDrawerItem(item.itemId)?.let { route ->
                    CategoryNavigation.navigateToCategory(navController, route)
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        binding.toolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        handleIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        return navHost.navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
