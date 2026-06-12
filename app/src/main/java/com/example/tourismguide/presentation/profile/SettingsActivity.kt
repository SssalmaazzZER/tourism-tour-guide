package com.example.tourismguide.presentation.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivitySettingsBinding
import com.example.tourismguide.data.local.AppDatabase
import com.example.tourismguide.domain.repository.AuthRepository
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.presentation.splash.SplashActivity
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri

@AndroidEntryPoint
class SettingsActivity : LocalizedActivity() {
    private lateinit var binding: ActivitySettingsBinding
    
    @Inject
    lateinit var dataStoreManager: DataStoreManager
    
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        
        setupUI()
        loadSettings()
        setupListeners()
    }

    private fun setupUI() {
        // Set version info
        val versionName = try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: Exception) {
            "1.0"
        }
        binding.textVersion.text = getString(R.string.version_format, versionName)
    }

    private fun loadSettings() {
        lifecycleScope.launch {
            try {
                // Load language preference
                val language = dataStoreManager.languagePreference.first() ?: "fr"
                when (language) {
                    "fr" -> binding.languageGroup.check(R.id.buttonFrench)
                    "ar" -> binding.languageGroup.check(R.id.buttonArabic)
                    "es" -> binding.languageGroup.check(R.id.buttonSpanish)
                    else -> binding.languageGroup.check(R.id.buttonEnglish)
                }
                
                // Load dark mode preference (use system default if not set)
                val darkModeValue = dataStoreManager.darkMode.first()
                binding.switchDarkMode.isChecked = darkModeValue ?: isSystemDarkModeEnabled()
                
                // Load notification preferences
                val notifyNearby = dataStoreManager.notifyNearbyLandmarks.first() ?: true
                val notifyBookings = dataStoreManager.notifyBookings.first() ?: true
                binding.switchNearby.isChecked = notifyNearby
                binding.switchBookings.isChecked = notifyBookings
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupListeners() {
        // Language selection
        binding.languageGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val language = when (checkedId) {
                    R.id.buttonFrench -> "fr"
                    R.id.buttonArabic -> "ar"
                    R.id.buttonSpanish -> "es"
                    else -> "en"
                }
                lifecycleScope.launch {
                    dataStoreManager.setLanguage(language)
                    Snackbar.make(
                        binding.root,
                        R.string.language_changed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // Recreate activity to apply language change
                    recreate()
                }
            }
        }

        // Dark mode toggle
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                dataStoreManager.setDarkMode(isChecked)
                val mode = if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }

        // Notification toggles
        binding.switchNearby.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                dataStoreManager.setNotifyNearbyLandmarks(isChecked)
            }
        }

        binding.switchBookings.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                dataStoreManager.setNotifyBookings(isChecked)
            }
        }

        // Clear cache button
        binding.buttonClearCache.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val cleared = database.placeDao().clearCachedPlaces()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.cache_cleared_count, cleared),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        R.string.error_generic,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Privacy policy button
        binding.buttonPrivacyPolicy.setOnClickListener {
            openUrl("https://tourism-guide.example.com/privacy")
        }

        // Rate app button
        binding.buttonRateApp.setOnClickListener {
            try {
                val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    R.string.error_generic,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        // Logout button
        binding.buttonLogout.setOnClickListener {
            lifecycleScope.launch {
                authRepository.signOut()
                startActivity(
                    Intent(this@SettingsActivity, SplashActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                finish()
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Snackbar.make(
                binding.root,
                R.string.error_generic,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun isSystemDarkModeEnabled(): Boolean {
        return resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
}
