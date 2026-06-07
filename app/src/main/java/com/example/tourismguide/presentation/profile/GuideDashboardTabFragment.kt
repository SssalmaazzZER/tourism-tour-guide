package com.example.tourismguide.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentGuideDashboardBinding
import com.example.tourismguide.service.GuideLocationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GuideDashboardTabFragment : Fragment() {
    private val viewModel: GuideDashboardViewModel by viewModels()
    private lateinit var adapter: GuideRequestAdapter
    private var _binding: FragmentGuideDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGuideDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GuideRequestAdapter(
            onAccept = { viewModel.accept(it) },
            onDecline = { viewModel.decline(it) }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.incomingRequests.collect { adapter.submitList(it) } }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            is GuideDashboardEvent.Accepted -> {
                                ContextCompat.startForegroundService(
                                    requireContext(),
                                    Intent(requireContext(), GuideLocationService::class.java)
                                        .putExtra(GuideLocationService.EXTRA_GUIDE_ID, event.guideId)
                                )
                                navigateToLiveTracking(event.requestId, event.guideId)
                            }
                            is GuideDashboardEvent.Error -> {
                                // keep the UI simple; future pass can surface a snackbar here
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToLiveTracking(requestId: String, guideId: String) {
        val navController = (requireActivity().supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        navController.navigate(
            R.id.liveTrackingFragment,
            bundleOf(
                "requestId" to requestId,
                "guideId" to guideId
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
