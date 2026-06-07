package com.example.tourismguide.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    // Initialize the ViewModel here so children can access it via requireParentFragment()
    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 4
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> BookingsTabFragment()
                1 -> PhotosTabFragment()
                2 -> ReviewsTabFragment()
                else -> GuideDashboardTabFragment()
            }
        }
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = listOf(
                getString(R.string.my_bookings),
                getString(R.string.my_photos),
                getString(R.string.my_reviews),
                getString(R.string.guide_dashboard)
            )[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
