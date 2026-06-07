package com.example.tourismguide.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tourismguide.databinding.ItemPlaceCardBinding
import com.example.tourismguide.domain.model.Place
import java.util.Locale

class PlaceCardAdapter(
    private val onPlaceClick: (Place, android.view.View) -> Unit
) : ListAdapter<Place, PlaceCardAdapter.PlaceViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder =
        PlaceViewHolder(ItemPlaceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) = holder.bind(getItem(position))

    inner class PlaceViewHolder(private val binding: ItemPlaceCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.imagePlace.load(place.imageUrl) { 
                crossfade(true) 
                // In a real app, we'd add parallax effect logic here or in a scroll listener
            }
            binding.textPlaceName.text = place.name
            binding.textCategory.text = place.category
            binding.textRating.text = String.format(Locale.US, "%.1f", place.rating)
            
            // Shared Element Transition Name
            binding.imagePlace.transitionName = "place_image_${place.id}"
            
            binding.root.setOnClickListener { onPlaceClick(place, binding.imagePlace) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Place, newItem: Place) = oldItem == newItem
    }
}
