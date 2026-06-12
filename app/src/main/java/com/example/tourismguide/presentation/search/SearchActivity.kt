package com.example.tourismguide.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.databinding.ActivitySearchBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.tourism.TourismDetailActivity
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : LocalizedActivity() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = SearchResultAdapter { content ->
            viewModel.submitSearch(binding.searchInput.text?.toString().orEmpty())
            startActivity(
                Intent(this, TourismDetailActivity::class.java)
                    .putExtra(TourismDetailActivity.EXTRA_CONTENT_ID, content.id)
            )
        }
        binding.resultsRecycler.layoutManager = LinearLayoutManager(this)
        binding.resultsRecycler.adapter = adapter

        binding.searchInput.doAfterTextChanged { viewModel.updateQuery(it?.toString().orEmpty()) }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.submitSearch(binding.searchInput.text?.toString().orEmpty())
                true
            } else false
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.results.collect { results ->
                        adapter.submitList(results)
                        binding.emptyState.isVisible = results.isEmpty() && viewModel.query.value.length >= 2
                    }
                }
                launch {
                    viewModel.recentSearches.collect { recent ->
                        binding.recentSearchesLabel.isVisible = recent.isNotEmpty()
                        binding.recentChips.removeAllViews()
                        recent.take(5).forEach { query ->
                            val chip = Chip(this@SearchActivity).apply {
                                text = query
                                setOnClickListener {
                                    binding.searchInput.setText(query)
                                    viewModel.submitSearch(query)
                                }
                            }
                            binding.recentChips.addView(chip)
                        }
                    }
                }
            }
        }
    }
}
