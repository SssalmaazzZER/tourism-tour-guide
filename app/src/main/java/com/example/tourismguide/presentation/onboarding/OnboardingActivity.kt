package com.example.tourismguide.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tourismguide.R
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.databinding.ActivityOnboardingBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : LocalizedActivity() {
    @Inject lateinit var dataStoreManager: DataStoreManager
    private lateinit var binding: ActivityOnboardingBinding
    private var selectedLanguage = "fr"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        binding.onboardingPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3
            override fun createFragment(position: Int) = OnboardingPageFragment.newInstance(position)
        }
        binding.languageGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) selectedLanguage = when (checkedId) {
                R.id.buttonArabic -> "ar"
                R.id.buttonEnglish -> "en"
                else -> "fr"
            }
        }
        binding.buttonGetStarted.setOnClickListener {
            lifecycleScope.launch {
                dataStoreManager.setLanguage(selectedLanguage)
                dataStoreManager.setFirstLaunchCompleted()
                startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
                finish()
            }
        }
        binding.onboardingPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.languageGroup.visibility = if (position == 2) android.view.View.VISIBLE else android.view.View.GONE
                binding.buttonGetStarted.visibility = if (position == 2) android.view.View.VISIBLE else android.view.View.GONE
            }
        })
        (binding.onboardingPager.getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}
