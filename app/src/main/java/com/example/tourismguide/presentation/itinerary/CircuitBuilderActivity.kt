package com.example.tourismguide.presentation.itinerary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityCircuitBuilderBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CircuitBuilderActivity : LocalizedActivity() {
    private val viewModel: CircuitBuilderViewModel by viewModels()
    private lateinit var binding: ActivityCircuitBuilderBinding
    private lateinit var adapter: ItineraryDayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircuitBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        viewModel.init(
            intent.getStringExtra(EXTRA_ITINERARY_ID).orEmpty(),
            intent.getStringExtra(EXTRA_CIRCUIT_ID)
        )

        adapter = ItineraryDayAdapter()
        binding.daysRecycler.layoutManager = LinearLayoutManager(this)
        binding.daysRecycler.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, from: RecyclerView.ViewHolder, to: RecyclerView.ViewHolder): Boolean {
                adapter.moveItem(from.bindingAdapterPosition, to.bindingAdapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val day = adapter.currentList[viewHolder.bindingAdapterPosition]
                viewModel.deleteDay(day.id)
            }

            override fun clearView(rv: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(rv, viewHolder)
                viewModel.reorderDays(adapter.currentList)
            }
        }).attachToRecyclerView(binding.daysRecycler)

        binding.fabAddDay.setOnClickListener { showAddDayDialog() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.days.collect { adapter.submitList(it) }
            }
        }
    }

    private fun showAddDayDialog() {
        val input = TextInputEditText(this).apply { hint = getString(R.string.day_title_hint) }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.add_day)
            .setView(input)
            .setPositiveButton(R.string.create) { _, _ ->
                viewModel.addDay(input.text?.toString().orEmpty().ifBlank { getString(R.string.new_day) })
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    companion object {
        const val EXTRA_ITINERARY_ID = "itineraryId"
        const val EXTRA_CIRCUIT_ID = "circuitId"
    }
}
