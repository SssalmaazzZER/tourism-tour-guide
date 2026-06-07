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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentHomeBinding
import com.example.tourismguide.presentation.home.adapter.CategoryAdapter
import com.example.tourismguide.presentation.home.adapter.CategoryItem
import com.example.tourismguide.presentation.home.adapter.SectionAdapter
import com.example.tourismguide.presentation.itinerary.ItineraryViewModel
import com.example.tourismguide.presentation.place.PlaceDetailActivity
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
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        observeState()
        
        // Setup Search Bar micro-interaction logic could go here
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter(categories()) { item ->
            viewModel.selectCategory(item.category)
        }
        
        sectionAdapter = SectionAdapter { place, sharedView ->
            val selectedItineraryId = itineraryViewModel.selectedItineraryId.value
            if (!selectedItineraryId.isNullOrBlank()) {
                itineraryViewModel.addPlaceToItinerary(selectedItineraryId, place.id)
                itineraryViewModel.clearSelection()
                val msg = getString(R.string.place_added_to_itinerary, place.name)
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
            } else {
                val intent = Intent(requireContext(), PlaceDetailActivity::class.java).apply {
                    putExtra("placeId", place.id)
                }
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    sharedView,
                    "place_image"
                )
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
                        state is HomeUiState.Content && state.sections.any { section -> section.places.isNotEmpty() }
                    binding.emptyState.isVisible =
                        state is HomeUiState.Content && state.sections.all { section -> section.places.isEmpty() }
                    
                    when (state) {
                        is HomeUiState.Content -> {
                            sectionAdapter.submitList(state.sections)
                        }
                        is HomeUiState.Error -> {
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                        HomeUiState.Loading -> Unit
                    }
                }
            }
        }
    }

    private fun categories() = listOf(
        CategoryItem(getString(R.string.category_all), null, R.drawable.ic_star_motif),
        CategoryItem(getString(R.string.category_culture), "CULTURE", R.drawable.ic_placeholder),
        CategoryItem(getString(R.string.category_food), "FOOD", R.drawable.ic_placeholder),
        CategoryItem(getString(R.string.category_city), "CITY", R.drawable.ic_placeholder),
        CategoryItem(getString(R.string.category_beach), "BEACH", R.drawable.ic_placeholder),
        CategoryItem(getString(R.string.category_activity), "ACTIVITY", R.drawable.ic_placeholder)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
