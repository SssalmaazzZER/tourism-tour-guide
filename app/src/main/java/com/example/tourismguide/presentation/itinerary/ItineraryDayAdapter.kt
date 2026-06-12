package com.example.tourismguide.presentation.itinerary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.databinding.ItemItineraryDayBinding
import com.example.tourismguide.data.local.entity.UserItineraryDayEntity
import java.util.Collections

class ItineraryDayAdapter : ListAdapter<UserItineraryDayEntity, ItineraryDayAdapter.ViewHolder>(Diff) {

    private val mutable = mutableListOf<UserItineraryDayEntity>()

    override fun submitList(list: List<UserItineraryDayEntity>?) {
        mutable.clear()
        if (list != null) mutable.addAll(list)
        super.submitList(list?.toList())
    }

    fun moveItem(from: Int, to: Int) {
        if (from == to) return
        Collections.swap(mutable, from, to)
        submitList(mutable.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemItineraryDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), position + 1)

    inner class ViewHolder(private val binding: ItemItineraryDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: UserItineraryDayEntity, number: Int) {
            binding.textDayNumber.text = number.toString()
            binding.textDayTitle.text = day.title
            binding.textDayDescription.text = day.description
        }
    }

    private object Diff : DiffUtil.ItemCallback<UserItineraryDayEntity>() {
        override fun areItemsTheSame(oldItem: UserItineraryDayEntity, newItem: UserItineraryDayEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: UserItineraryDayEntity, newItem: UserItineraryDayEntity) = oldItem == newItem
    }
}
