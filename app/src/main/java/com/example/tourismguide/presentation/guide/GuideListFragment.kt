package com.example.tourismguide.presentation.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.databinding.FragmentGuideListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GuideListFragment : Fragment() {
    private val viewModel: GuideListViewModel by viewModels()
    private var _binding: FragmentGuideListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GuideAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGuideListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GuideAdapter(
            onOpen = { startActivity(Intent(requireContext(), GuideDetailActivity::class.java).putExtra("guideId", it.id)) },
            onHire = { GuideRequestBottomSheet.newInstance(it.id).show(parentFragmentManager, GuideRequestBottomSheet.TAG) }
        )
        binding.guideRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.guideRecycler.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query.orEmpty())
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText.orEmpty())
                return true
            }
        })
        binding.fabFilters.setOnClickListener {
            FilterBottomSheet.newInstance(viewModel.filters.value).show(parentFragmentManager, FilterBottomSheet.TAG)
        }
        parentFragmentManager.setFragmentResultListener(FilterBottomSheet.RESULT_KEY, viewLifecycleOwner) { _, bundle ->
            @Suppress("DEPRECATION")
            viewModel.applyFilters(bundle.getSerializable(FilterBottomSheet.ARG_FILTERS) as? GuideFilters ?: GuideFilters())
        }
        observe()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.guides.collect {
                    binding.progressBar.isVisible = it is NetworkResult.Loading
                    binding.emptyState.isVisible = it is NetworkResult.Success && it.data.isEmpty()
                    if (it is NetworkResult.Success) adapter.submitList(it.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
