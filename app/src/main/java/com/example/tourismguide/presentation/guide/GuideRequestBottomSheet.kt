package com.example.tourismguide.presentation.guide

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tourismguide.R
import com.example.tourismguide.databinding.BottomSheetGuideRequestBinding
import com.example.tourismguide.presentation.main.MainActivity
import com.example.tourismguide.util.ImageUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class GuideRequestBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: GuideRequestViewModel by viewModels()
    private var _binding: BottomSheetGuideRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetGuideRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSend.isEnabled = false
        binding.sliderDuration.addOnChangeListener { _, value, _ -> viewModel.setDuration(value.toInt()); updateEstimate() }
        binding.buttonPlus.setOnClickListener { viewModel.incrementPeople(); updatePeople() }
        binding.buttonMinus.setOnClickListener { viewModel.decrementPeople(); updatePeople() }
        binding.buttonDate.setOnClickListener { showDatePicker() }
        binding.buttonTime.setOnClickListener { showTimePicker() }
        binding.buttonSend.setOnClickListener { viewModel.sendRequest(binding.inputRequests.editText?.text.toString()) }
        observe()
        updateSendEnabled()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            is GuideRequestEvent.NavigateToLiveTracking -> {
                                startActivity(
                                    Intent(requireContext(), MainActivity::class.java)
                                        .putExtra("openLiveTracking", true)
                                        .putExtra("requestId", event.requestId)
                                        .putExtra("guideId", event.guideId)
                                )
                                dismissAllowingStateLoss()
                            }
                            is GuideRequestEvent.Error -> Unit
                        }
                    }
                }
                launch {
                    viewModel.state.collect { state ->
                        binding.progressBar.isVisible = state.isLoading
                        binding.textName.text = state.guideName
                        binding.textRating.text = getString(R.string.rating_per_hour, state.guideRating, state.guidePricePerHour)
                        ImageUtils.loadWithCoil(binding.imageAvatar, state.guideAvatarUrl)
                        binding.buttonSend.isEnabled = viewModel.selectedDate.value > 0 && viewModel.selectedTime.value > 0 && !state.isLoading
                        binding.textAddress.text = state.address.ifBlank { getString(R.string.address_not_available) }
                        state.error?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() }
                        if (state.request != null) dismissAllowingStateLoss()
                        updateSendEnabled()
                    }
                }
                launch {
                    viewModel.estimatedPrice.collect { binding.textPriceEstimate.text = getString(R.string.price_estimate_mad, it) }
                }
            }
        }
    }

    private fun updatePeople() {
        binding.textPeopleCount.text = viewModel.peopleCount.value.toString()
        binding.textPriceEstimate.text = getString(R.string.price_estimate_mad, viewModel.estimatedPrice.value)
    }

    private fun updateEstimate() {
        binding.textDuration.text = resources.getQuantityString(R.plurals.hours, viewModel.durationHours.value, viewModel.durationHours.value)
    }

    private fun showDatePicker() {
        val now = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            val date = Calendar.getInstance().apply { set(y, m, d) }.timeInMillis
            viewModel.setDate(date)
            binding.textDate.text = getString(R.string.date_selected)
            updateSendEnabled()
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, hour, minute ->
            val time = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, hour); set(Calendar.MINUTE, minute) }.timeInMillis
            viewModel.setTime(time)
            binding.textTime.text = getString(R.string.time_selected)
            updateSendEnabled()
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
    }

    private fun updateSendEnabled() {
        binding.buttonSend.isEnabled = viewModel.selectedDate.value > 0 && viewModel.selectedTime.value > 0 && !binding.progressBar.isVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "GuideRequestBottomSheet"
        fun newInstance(guideId: String) = GuideRequestBottomSheet().apply {
            arguments = Bundle().apply { putString("guideId", guideId) }
        }
    }
}
