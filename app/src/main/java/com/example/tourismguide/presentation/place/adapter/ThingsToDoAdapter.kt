package com.example.tourismguide.presentation.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.databinding.ItemThingToDoBinding

class ThingsToDoAdapter : ListAdapter<String, ThingsToDoAdapter.ThingViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThingViewHolder =
        ThingViewHolder(ItemThingToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ThingViewHolder, position: Int) = holder.bind(getItem(position))

    class ThingViewHolder(private val binding: ItemThingToDoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.checkbox.text = text
        }
    }

    private object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
