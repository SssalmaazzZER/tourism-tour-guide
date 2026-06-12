package com.example.tourismguide.presentation.itinerary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.databinding.ItemItineraryTicketBinding

class ItineraryAdapter(
    private val onClick: (ItineraryListItem) -> Unit,
    private val onCustomize: (ItineraryListItem) -> Unit
) : ListAdapter<ItineraryListItem, ItineraryAdapter.ItineraryViewHolder>(Diff) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder =
        ItineraryViewHolder(ItemItineraryTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        holder.bind(getItem(position))
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: android.view.View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.fade_in)
            animation.startOffset = (position * 100).toLong()
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    inner class ItineraryViewHolder(private val binding: ItemItineraryTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItineraryListItem) {
            binding.textTitle.text = item.itinerary.name
            binding.textDay.text = "Jour ${bindingAdapterPosition + 1}"
            // Description could be mapped from itinerary metadata or a sample
            binding.textDescription.text = "Exploration de la culture locale et des monuments historiques."

            binding.root.setOnClickListener { onClick(item) }
            binding.buttonCustomize.setOnClickListener { onCustomize(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<ItineraryListItem>() {
        override fun areItemsTheSame(oldItem: ItineraryListItem, newItem: ItineraryListItem) = oldItem.itinerary.id == newItem.itinerary.id
        override fun areContentsTheSame(oldItem: ItineraryListItem, newItem: ItineraryListItem) = oldItem == newItem
    }
}
