package com.example.tourismguide.presentation.itinerary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.PlaceEntity
import com.example.tourismguide.databinding.ItemItineraryPlaceBinding
import com.example.tourismguide.util.ImageUtils

class ItineraryDetailPlaceAdapter : RecyclerView.Adapter<ItineraryDetailPlaceAdapter.VH>() {
    private val items = mutableListOf<PlaceEntity>()

    fun submitList(list: List<PlaceEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun moveItem(from: Int, to: Int) {
        if (from !in items.indices || to !in items.indices) return
        val item = items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }

    fun currentIds(): List<String> = items.map { it.id }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemItineraryPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemItineraryPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceEntity) {
            ImageUtils.loadWithCoil(binding.imagePlace, item.imageUrl)
            binding.textName.text = item.name
            binding.textCategory.text = item.category
        }
    }
}
