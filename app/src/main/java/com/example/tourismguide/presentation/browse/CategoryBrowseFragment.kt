package com.example.tourismguide.presentation.browse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentCategoryBrowseBinding
import com.example.tourismguide.presentation.discover.TourismContentAdapter
import com.example.tourismguide.presentation.tourism.TourismDetailActivity
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryBrowseFragment : Fragment() {
    private val viewModel: CategoryBrowseViewModel by viewModels()
    private var _binding: FragmentCategoryBrowseBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TourismContentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val titleRes = arguments?.getInt("titleRes")?.takeIf { it != 0 } ?: R.string.discover_morocco
        binding.toolbar.setTitle(titleRes)
        binding.toolbar.setNavigationOnClickListener {
            if (!findNavController().popBackStack()) requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter = TourismContentAdapter { content ->
            startActivity(
                Intent(requireContext(), TourismDetailActivity::class.java)
                    .putExtra(TourismDetailActivity.EXTRA_CONTENT_ID, content.id)
            )
        }
        binding.contentRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.contentRecycler.adapter = adapter

        setupFilters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { items ->
                    adapter.submitList(items)
                    binding.emptyState.isVisible = items.isEmpty()
                }
            }
        }
    }

    private fun setupFilters() {
        val ratings = listOf(
            null to getString(R.string.category_all),
            4.0 to "4.0+",
            4.5 to "4.5+"
        )
        ratings.forEach { (rating, label) ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCheckable = true
                setOnCheckedChangeListener { _, checked ->
                    if (checked) viewModel.applyRatingFilter(rating ?: 0.0)
                }
            }
            binding.filterChips.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
