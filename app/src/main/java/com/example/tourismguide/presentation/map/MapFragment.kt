package com.example.tourismguide.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tourismguide.R
import com.example.tourismguide.databinding.FragmentMapBinding
import com.example.tourismguide.presentation.place.PlaceDetailActivity
import com.example.tourismguide.service.GeofenceBroadcastReceiver
import com.example.tourismguide.service.LocationService
import com.example.tourismguide.util.MapUtils
import com.example.tourismguide.util.PermissionHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MapViewModel by viewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private lateinit var placeClusterManager: ClusterManager<PlaceClusterItem>
    private lateinit var guideClusterManager: ClusterManager<GuideClusterItem>
    private lateinit var geofencingClient: GeofencingClient
    private var firstFixCentered = false
    private var selectedPlaceItem: PlaceClusterItem? = null

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) enableLocationFeatures() else Toast.makeText(requireContext(), R.string.location_permission_denied, Toast.LENGTH_LONG).show()
    }

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val lat = intent.getDoubleExtra(LocationService.EXTRA_LATITUDE, 0.0)
            val lng = intent.getDoubleExtra(LocationService.EXTRA_LONGITUDE, 0.0)
            val location = UserLocation(lat, lng)
            viewModel.updateUserLocation(location)
            if (!firstFixCentered) {
                firstFixCentered = true
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 13f))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        geofencingClient = LocationServices.getGeofencingClient(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.layerToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) renderActiveLayer(checkedId)
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(locationReceiver, IntentFilter(LocationService.ACTION_LOCATION_UPDATE))
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(locationReceiver)
        requireContext().stopService(Intent(requireContext(), LocationService::class.java))
        super.onStop()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        placeClusterManager = ClusterManager(requireContext(), map)
        guideClusterManager = ClusterManager(requireContext(), map)
        guideClusterManager.renderer = GuideMarkerRenderer(requireContext(), map, guideClusterManager)
        map.setOnCameraIdleListener {
            placeClusterManager.onCameraIdle()
            guideClusterManager.onCameraIdle()
        }
        map.setOnMarkerClickListener { marker ->
            placeClusterManager.onMarkerClick(marker) || guideClusterManager.onMarkerClick(marker)
        }
        map.setOnInfoWindowClickListener {
            selectedPlaceItem?.let { item ->
                startActivity(Intent(requireContext(), PlaceDetailActivity::class.java).putExtra("placeId", item.place.id))
            }
        }
        placeClusterManager.renderer = object : com.google.maps.android.clustering.view.DefaultClusterRenderer<PlaceClusterItem>(requireContext(), map, placeClusterManager) {
            override fun onBeforeClusterItemRendered(item: PlaceClusterItem, markerOptions: com.google.android.gms.maps.model.MarkerOptions) {
                markerOptions.icon(MapUtils.vectorToBitmapDescriptor(requireContext(), R.drawable.ic_place_marker))
            }
        }
        placeClusterManager.setOnClusterItemClickListener {
            selectedPlaceItem = it
            false
        }
        guideClusterManager.setOnClusterItemClickListener {
            Toast.makeText(requireContext(), R.string.phase_3_coming, Toast.LENGTH_SHORT).show()
            true
        }
        if (PermissionHelper.checkPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableLocationFeatures()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        observeLayers()
    }

    private fun observeLayers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.placesState.collect { if (binding.buttonPlaces.isChecked) renderPlaces(it) }
                }
                launch {
                    viewModel.guidesState.collect { if (binding.buttonGuides.isChecked) renderGuides(it) }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationFeatures() {
        googleMap?.isMyLocationEnabled = true
        ContextCompat.startForegroundService(requireContext(), Intent(requireContext(), LocationService::class.java))
        val places = (viewModel.placesState.value as? LayerState.Success)?.data.orEmpty()
        addGeofences(places.take(10))
    }

    private fun renderActiveLayer(checkedId: Int) {
        if (checkedId == R.id.buttonGuides) renderGuides(viewModel.guidesState.value) else renderPlaces(viewModel.placesState.value)
    }

    private fun renderPlaces(state: PlacesState) {
        placeClusterManager.clearItems()
        guideClusterManager.clearItems()
        if (state is LayerState.Success) {
            placeClusterManager.addItems(state.data.map { PlaceClusterItem(it, getString(R.string.open)) })
            addGeofences(state.data.take(10))
        }
        placeClusterManager.cluster()
    }

    private fun renderGuides(state: GuidesState) {
        placeClusterManager.clearItems()
        guideClusterManager.clearItems()
        if (state is LayerState.Success) guideClusterManager.addItems(state.data.map { GuideClusterItem(it) })
        guideClusterManager.cluster()
    }

    @SuppressLint("MissingPermission")
    private fun addGeofences(places: List<com.example.tourismguide.domain.model.Place>) {
        if (!PermissionHelper.checkPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) || places.isEmpty()) return
        val geofences = places.map {
            Geofence.Builder()
                .setRequestId("${it.id}|${it.name}")
                .setCircularRegion(it.latitude, it.longitude, 100f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()
        }
        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofences)
            .build()
        geofencingClient.addGeofences(request, geofencePendingIntent())
    }

    private fun geofencePendingIntent(): PendingIntent {
        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(requireContext(), 2001, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
