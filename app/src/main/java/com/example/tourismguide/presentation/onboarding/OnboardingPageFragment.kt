package com.example.tourismguide.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentOnboardingPageBinding

class OnboardingPageFragment : Fragment() {
    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val index = requireArguments().getInt(ARG_INDEX)
        val titles = listOf(R.string.onboarding_title_1, R.string.onboarding_title_2, R.string.onboarding_title_3)
        val bodies = listOf(R.string.onboarding_body_1, R.string.onboarding_body_2, R.string.onboarding_body_3)
        binding.textTitle.setText(titles[index])
        binding.textBody.setText(bodies[index])
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_INDEX = "index"
        fun newInstance(index: Int) = OnboardingPageFragment().apply {
            arguments = Bundle().apply { putInt(ARG_INDEX, index) }
        }
    }
}
