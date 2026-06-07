package com.example.tourismguide.presentation.itinerary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.databinding.ItemItineraryPlaceBinding
import com.example.tourismguide.util.ImageUtils

class ItineraryPlaceAdapter(
    private val onMoveRequested: (fromPosition: Int, toPosition: Int) -> Unit
) : ListAdapter<PlaceEntity, ItineraryPlaceAdapter.VH>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemItineraryPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemItineraryPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceEntity) {
            ImageUtils.loadWithCoil(binding.imagePlace, item.imageUrl)
            binding.textName.text = item.name
            binding.textCategory.text = item.category
            binding.root.setOnLongClickListener {
                val current = bindingAdapterPosition
                if (current != RecyclerView.NO_POSITION) onMoveRequested(current, (current + 1).coerceAtMost(itemCount - 1))
                true
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<PlaceEntity>() {
        override fun areItemsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity) = oldItem == newItem
    }
}
