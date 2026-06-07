package com.example.tourismguide.presentation.itinerary

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.BottomSheetSaveItineraryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaveToItineraryBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: ItineraryViewModel by viewModels()
    private var _binding: BottomSheetSaveItineraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ItineraryPickerAdapter
    private val placeId get() = requireArguments().getString(ARG_PLACE_ID).orEmpty()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetSaveItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ItineraryPickerAdapter {
            viewModel.addPlaceToItinerary(it.id, placeId)
            dismiss()
        }
        binding.itineraryRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.itineraryRecycler.adapter = adapter
        binding.buttonCreateItinerary.setOnClickListener { showCreateDialog() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.itineraries.collect { adapter.submitList(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCreateDialog() {
        val input = EditText(requireContext()).apply { hint = getString(R.string.itinerary_name) }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.create_new_itinerary)
            .setView(input)
            .setPositiveButton(R.string.create) { _, _ ->
                viewModel.createAndAdd(input.text.toString().ifBlank { getString(R.string.new_itinerary) }, placeId)
                dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    companion object {
        const val TAG = "SaveToItineraryBottomSheet"
        private const val ARG_PLACE_ID = "placeId"
        fun newInstance(placeId: String) = SaveToItineraryBottomSheet().apply {
            arguments = Bundle().apply { putString(ARG_PLACE_ID, placeId) }
        }
    }
}
