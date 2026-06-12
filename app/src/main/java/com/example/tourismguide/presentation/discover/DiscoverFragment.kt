package com.example.tourismguide.presentation.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.databinding.FragmentDiscoverBinding
import com.example.tourismguide.presentation.navigation.CategoryNavigation
import com.example.tourismguide.presentation.search.SearchActivity
import com.example.tourismguide.presentation.tourism.TourismDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoverFragment : Fragment() {
    private val viewModel: DiscoverViewModel by viewModels()
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: DiscoverCategoryAdapter
    private lateinit var featuredAdapter: TourismContentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryAdapter = DiscoverCategoryAdapter { route ->
            CategoryNavigation.navigateToCategory(findNavController(), route)
        }
        featuredAdapter = TourismContentAdapter { openDetail(it.id) }

        binding.categoryGrid.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.categoryGrid.adapter = categoryAdapter
        categoryAdapter.submitList(
            viewModel.categories.map { route ->
                DiscoverCategoryAdapter.Item(route, iconFor(route.contentType))
            }
        )

        binding.featuredRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.featuredRecycler.adapter = featuredAdapter

        binding.searchCard.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.featured.collect { featuredAdapter.submitList(it) }
            }
        }
    }

    private fun openDetail(contentId: String) {
        startActivity(
            Intent(requireContext(), TourismDetailActivity::class.java)
                .putExtra(TourismDetailActivity.EXTRA_CONTENT_ID, contentId)
        )
    }

    private fun iconFor(type: String?) = when (type) {
        "CITY" -> com.example.tourismguide.R.drawable.ic_home
        "MONUMENT" -> com.example.tourismguide.R.drawable.ic_star_motif
        "CULTURE" -> com.example.tourismguide.R.drawable.ic_people
        "GASTRONOMY" -> com.example.tourismguide.R.drawable.ic_add
        "NATURE" -> com.example.tourismguide.R.drawable.ic_map
        "ACTIVITY" -> com.example.tourismguide.R.drawable.ic_route
        "FESTIVAL" -> com.example.tourismguide.R.drawable.ic_mic
        else -> com.example.tourismguide.R.drawable.ic_star_motif
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
