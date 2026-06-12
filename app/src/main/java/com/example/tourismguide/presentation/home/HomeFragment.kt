package com.example.tourismguide.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentHomeBinding
import com.example.tourismguide.presentation.home.adapter.CategoryAdapter
import com.example.tourismguide.presentation.home.adapter.CategoryItem
import com.example.tourismguide.presentation.home.adapter.SectionAdapter
import com.example.tourismguide.presentation.itinerary.ItineraryViewModel
import com.example.tourismguide.presentation.navigation.CategoryNavigation
import com.example.tourismguide.presentation.place.PlaceDetailActivity
import com.example.tourismguide.presentation.search.SearchActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private val itineraryViewModel: ItineraryViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sectionAdapter: SectionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapters()
        observeState()
        binding.searchBar.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        binding.cardFindGuide.setOnClickListener {
            findNavController().navigate(R.id.guideListFragment)
        }
    }

    private fun setupAdapters() {
        val categoryItems = CategoryNavigation.homeQuickCategories.map { route ->
            CategoryItem(
                label = getString(route.titleRes),
                category = route.contentType,
                titleRes = route.titleRes,
                iconRes = iconFor(route.contentType)
            )
        }
        categoryAdapter = CategoryAdapter(categoryItems) { item ->
            if (item.category == null) {
                viewModel.selectCategory(null)
            } else {
                CategoryNavigation.navigateToCategory(
                    findNavController(),
                    CategoryNavigation.CategoryRoute(item.category, item.titleRes)
                )
            }
        }

        sectionAdapter = SectionAdapter { place, sharedView ->
            val selectedItineraryId = itineraryViewModel.selectedItineraryId.value
            if (!selectedItineraryId.isNullOrBlank()) {
                itineraryViewModel.addPlaceToItinerary(selectedItineraryId, place.id)
                itineraryViewModel.clearSelection()
                Snackbar.make(binding.root, getString(R.string.place_added_to_itinerary, place.name), Snackbar.LENGTH_LONG).show()
            } else {
                val intent = Intent(requireContext(), PlaceDetailActivity::class.java).putExtra("placeId", place.id)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), sharedView, "place_image")
                startActivity(intent, options.toBundle())
            }
        }

        binding.categoryRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        binding.sectionRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sectionAdapter
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.sectionRecycler.isVisible =
                        state is HomeUiState.Content && state.sections.any { it.places.isNotEmpty() }
                    binding.emptyState.isVisible =
                        state is HomeUiState.Content && state.sections.all { it.places.isEmpty() }
                    when (state) {
                        is HomeUiState.Content -> sectionAdapter.submitList(state.sections)
                        is HomeUiState.Error -> Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        HomeUiState.Loading -> Unit
                    }
                }
            }
        }
    }

    private fun iconFor(type: String?) = when (type) {
        "CITY" -> R.drawable.ic_home
        "MONUMENT" -> R.drawable.ic_star_motif
        "CULTURE" -> R.drawable.ic_people
        "GASTRONOMY" -> R.drawable.ic_add
        "NATURE" -> R.drawable.ic_map
        "ACTIVITY" -> R.drawable.ic_route
        "FESTIVAL" -> R.drawable.ic_mic
        else -> R.drawable.ic_star_motif
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
