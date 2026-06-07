package com.example.tourismguide.presentation.itinerary

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityItineraryDetailBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.main.MainActivity
import com.example.tourismguide.util.MapUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItineraryDetailActivity : LocalizedActivity(), OnMapReadyCallback {
    private val viewModel: ItineraryViewModel by viewModels()
    private lateinit var binding: ActivityItineraryDetailBinding
    private lateinit var adapter: ItineraryDetailPlaceAdapter
    private var googleMap: GoogleMap? = null
    private var mapReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItineraryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        adapter = ItineraryDetailPlaceAdapter()
        binding.recyclerPlaces.layoutManager = LinearLayoutManager(this)
        binding.recyclerPlaces.adapter = adapter
        
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        
        binding.fabAddPlace.setOnClickListener {
            val itineraryId = viewModel.itinerary.value?.id ?: return@setOnClickListener
            startActivity(
                Intent(this, MainActivity::class.java)
                    .putExtra("selectItineraryId", itineraryId)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
        
        attachDragToReorder()
        observe()
        
        val itineraryId = intent.getStringExtra("itineraryId")
        if (itineraryId != null) {
            viewModel.startSelection(itineraryId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        mapReady = true
        map.uiSettings.isZoomControlsEnabled = true
        renderMarkers(viewModel.places.value)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.itinerary.collect { itinerary ->
                        supportActionBar?.title = itinerary?.name ?: getString(R.string.itinerary_detail)
                    }
                }
                launch {
                    viewModel.places.collect { places ->
                        adapter.submitList(places)
                        binding.emptyState.isVisible = places.isEmpty()
                        binding.recyclerPlaces.isVisible = places.isNotEmpty()
                        renderMarkers(places)
                    }
                }
            }
        }
    }

    private fun attachDragToReorder() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = viewHolder.bindingAdapterPosition
                val to = target.bindingAdapterPosition
                adapter.moveItem(from, to)
                viewModel.updatePlaceOrder(viewModel.itinerary.value?.id.orEmpty(), adapter.currentIds())
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit
        }).attachToRecyclerView(binding.recyclerPlaces)
    }

    private fun renderMarkers(places: List<com.example.tourismguide.data.local.entity.PlaceEntity>) {
        val map = googleMap ?: return
        if (!mapReady || places.isEmpty()) {
            binding.mapFallback.isVisible = true
            return
        }
        binding.mapFallback.isVisible = false
        map.clear()
        val boundsBuilder = LatLngBounds.Builder()
        places.forEach { place ->
            val position = LatLng(place.latitude, place.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(place.name)
                    .icon(MapUtils.vectorToBitmapDescriptor(this, R.drawable.ic_place_marker))
            )
            boundsBuilder.include(position)
        }
        runCatching {
            if (places.size == 1) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(places.first().latitude, places.first().longitude), 14f))
            } else {
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 80))
            }
        }.onFailure {
            binding.mapFallback.isVisible = true
        }
    }

    override fun onStart() { super.onStart(); binding.mapView.onStart() }
    override fun onResume() { super.onResume(); binding.mapView.onResume() }
    override fun onPause() { binding.mapView.onPause(); super.onPause() }
    override fun onStop() { binding.mapView.onStop(); super.onStop() }
    override fun onDestroy() { binding.mapView.onDestroy(); super.onDestroy() }
    override fun onLowMemory() { super.onLowMemory(); binding.mapView.onLowMemory() }
}
