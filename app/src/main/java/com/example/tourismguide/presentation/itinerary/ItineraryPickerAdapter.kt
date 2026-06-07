package com.example.tourismguide.presentation.itinerary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.ItineraryEntity
import com.example.tourismguide.databinding.ItemItineraryPickerBinding

class ItineraryPickerAdapter(
    private val onClick: (ItineraryEntity) -> Unit
) : ListAdapter<ItineraryEntity, ItineraryPickerAdapter.VH>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemItineraryPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemItineraryPickerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItineraryEntity) {
            binding.textName.text = item.name
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<ItineraryEntity>() {
        override fun areItemsTheSame(oldItem: ItineraryEntity, newItem: ItineraryEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ItineraryEntity, newItem: ItineraryEntity) = oldItem == newItem
    }
}
