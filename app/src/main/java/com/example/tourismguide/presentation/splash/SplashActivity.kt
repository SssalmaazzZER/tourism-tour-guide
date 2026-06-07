package com.example.tourismguide.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.databinding.ActivitySplashBinding
import com.example.tourismguide.domain.repository.AuthRepository
import com.example.tourismguide.presentation.auth.LoginActivity
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.main.MainActivity
import com.example.tourismguide.presentation.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : LocalizedActivity() {
    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var authRepository: AuthRepository
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        lifecycleScope.launch {
            val destination = when {
                dataStoreManager.isFirstLaunch().first() -> OnboardingActivity::class.java
                authRepository.isLoggedIn().first() -> MainActivity::class.java
                else -> LoginActivity::class.java
            }
            startActivity(Intent(this@SplashActivity, destination))
            finish()
        }
    }
}
