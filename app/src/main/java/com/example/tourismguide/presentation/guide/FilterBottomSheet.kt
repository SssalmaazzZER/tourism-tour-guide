package com.example.tourismguide.presentation.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tourismguide.databinding.BottomSheetGuideFiltersBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetGuideFiltersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetGuideFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filters = requireArguments().getSerializable(ARG_FILTERS) as? GuideFilters ?: GuideFilters()
        binding.chipArabic.isChecked = "AR" in filters.languages
        binding.chipFrench.isChecked = "FR" in filters.languages
        binding.chipEnglish.isChecked = "EN" in filters.languages
        binding.sliderRating.value = filters.minRating.toFloat()
        binding.sliderPrice.value = filters.maxPrice.toFloat()
        binding.buttonApply.setOnClickListener {
            val languages = buildSet {
                if (binding.chipArabic.isChecked) add("AR")
                if (binding.chipFrench.isChecked) add("FR")
                if (binding.chipEnglish.isChecked) add("EN")
            }
            send(GuideFilters(languages, binding.sliderRating.value.toDouble(), binding.sliderPrice.value.toDouble()))
        }
        binding.buttonReset.setOnClickListener { send(GuideFilters()) }
    }

    private fun send(filters: GuideFilters) {
        parentFragmentManager.setFragmentResult(RESULT_KEY, Bundle().apply { putSerializable(ARG_FILTERS, filters) })
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FilterBottomSheet"
        const val RESULT_KEY = "guide_filters_result"
        const val ARG_FILTERS = "filters"
        fun newInstance(filters: GuideFilters) = FilterBottomSheet().apply {
            arguments = Bundle().apply { putSerializable(ARG_FILTERS, filters) }
        }
    }
}
