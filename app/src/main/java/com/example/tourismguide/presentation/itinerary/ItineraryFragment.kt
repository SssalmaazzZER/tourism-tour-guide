package com.example.tourismguide.presentation.itinerary

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentItineraryBinding
import com.example.tourismguide.presentation.place.PlaceDetailActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItineraryFragment : Fragment() {
    private val viewModel: ItineraryViewModel by activityViewModels()
    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ItineraryAdapter
    private var lastDeleted: ItineraryListItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ItineraryAdapter(
            onClick = {
                startActivity(Intent(requireContext(), ItineraryDetailActivity::class.java).putExtra("itineraryId", it.itinerary.id))
            },
            onCustomize = {
                startActivity(
                    Intent(requireContext(), CircuitBuilderActivity::class.java)
                        .putExtra(CircuitBuilderActivity.EXTRA_ITINERARY_ID, it.itinerary.id)
                )
            }
        )
        binding.recyclerItineraries.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerItineraries.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        binding.fabCreate.setOnClickListener { showCreateDialog() }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList.getOrNull(viewHolder.bindingAdapterPosition)
                if (item != null) {
                    lastDeleted = item
                    viewModel.deleteItinerary(item.itinerary)
                    Snackbar.make(binding.root, R.string.itinerary_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo) {
                            lastDeleted?.let { viewModel.restoreItinerary(it.itinerary) }
                        }
                        .show()
                }
            }
        }).attachToRecyclerView(binding.recyclerItineraries)
        observe()
    }

    private fun showCreateDialog() {
        val input = EditText(requireContext()).apply { hint = getString(R.string.itinerary_name) }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.create_new_itinerary)
            .setView(input)
            .setPositiveButton(R.string.create) { _, _ ->
                viewModel.createItinerary(input.text.toString().ifBlank { getString(R.string.new_itinerary) })
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.itineraryItems.collect { items ->
                        binding.swipeRefresh.isRefreshing = false
                        binding.emptyState.isVisible = items.isEmpty()
                        binding.recyclerItineraries.isVisible = items.isNotEmpty()
                        adapter.submitList(items)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
