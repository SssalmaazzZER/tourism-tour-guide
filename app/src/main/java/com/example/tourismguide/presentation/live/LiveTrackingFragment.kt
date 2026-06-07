package com.example.tourismguide.presentation.live

import android.Manifest
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentLiveTrackingBinding
import com.example.tourismguide.util.MapUtils
import com.example.tourismguide.util.PermissionHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LiveTrackingFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: LiveTrackingViewModel by viewModels()
    private var _binding: FragmentLiveTrackingBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var userMarker: Marker? = null
    private var guideMarker: Marker? = null
    private var lastGuideLatLng: LatLng? = null

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) viewModel.startUserLocationTracking()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLiveTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (childFragmentManager.findFragmentById(R.id.liveMapContainer) as SupportMapFragment).getMapAsync(this)
        binding.buttonChat.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), com.example.tourismguide.presentation.chat.ChatActivity::class.java).putExtra("bookingId", viewModel.requestId))
        }
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        observe()
        if (PermissionHelper.checkPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            viewModel.startUserLocationTracking()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMapLoadedCallback { refreshMapPosition() }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.guide.collect { guide ->
                        binding.textGuideName.text = guide?.name.orEmpty()
                        val avatarUrl = guide?.avatarUrl
                        if (!avatarUrl.isNullOrBlank()) {
                            com.example.tourismguide.util.ImageUtils.loadWithCoil(binding.imageGuide, avatarUrl)
                        } else {
                            binding.imageGuide.setImageResource(R.drawable.ic_placeholder)
                        }
                    }
                }
                launch {
                    viewModel.guideLocation.collect { guideLocation ->
                        val target = guideLocation?.let { LatLng(it.latitude, it.longitude) }
                        if (target != null) animateGuideMarker(target) else guideMarker?.remove()
                        refreshMapPosition()
                    }
                }
                launch {
                    viewModel.userLocation.collect { user ->
                        if (user != null) {
                            if (userMarker == null) {
                                userMarker = map?.addMarker(
                                    com.google.android.gms.maps.model.MarkerOptions()
                                        .position(user)
                                        .title(getString(R.string.you))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                )
                            } else {
                                userMarker?.position = user
                            }
                            refreshMapPosition()
                        }
                    }
                }
                launch {
                    viewModel.etaText.collect { binding.textEta.text = it }
                }
                launch {
                    viewModel.requestStatus.collect { status ->
                        binding.textStatus.text = status.ifBlank { getString(R.string.live_tracking_title) }
                        if (status == "COMPLETED") {
                            findNavController().navigate(R.id.profileFragment)
                        }
                    }
                }
                launch {
                    viewModel.canCancel.collect { binding.buttonCancel.isVisible = it }
                }
            }
        }
    }

    private fun animateGuideMarker(target: LatLng) {
        val current = guideMarker?.position
        if (guideMarker == null) {
            guideMarker = map?.addMarker(
                com.google.android.gms.maps.model.MarkerOptions()
                    .position(target)
                    .title(getString(R.string.guides))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            lastGuideLatLng = target
            return
        }
        if (current == null || current == target) {
            guideMarker?.position = target
            return
        }
        val animator = ValueAnimator.ofFloat(0f, 1f).apply { duration = 4000L }
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedValue as Float
            val lat = current.latitude + (target.latitude - current.latitude) * fraction
            val lng = current.longitude + (target.longitude - current.longitude) * fraction
            guideMarker?.position = LatLng(lat, lng)
        }
        animator.start()
        lastGuideLatLng = target
    }

    private fun refreshMapPosition() {
        val map = map ?: return
        val points = listOfNotNull(userMarker?.position, guideMarker?.position)
        if (points.isNotEmpty()) {
            val first = points.first()
            if (userMarker?.position != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(first, 14f))
            }
        }
    }

    override fun onDestroyView() {
        viewModel.stopUserLocationTracking()
        _binding = null
        super.onDestroyView()
    }
}
