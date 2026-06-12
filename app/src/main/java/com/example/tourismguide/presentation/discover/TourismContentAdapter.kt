package com.example.tourismguide.presentation.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tourismguide.databinding.ItemPlaceCardBinding
import com.example.tourismguide.domain.model.TourismContent
import java.util.Locale

class TourismContentAdapter(
    private val onClick: (TourismContent) -> Unit
) : ListAdapter<TourismContent, TourismContentAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPlaceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemPlaceCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TourismContent) {
            binding.imagePlace.load(item.imageUrl) { crossfade(true) }
            binding.textPlaceName.text = item.title
            binding.textCategory.text = item.subtitle.ifBlank { item.contentType }
            binding.textRating.text = if (item.rating > 0) String.format(Locale.US, "%.1f", item.rating) else "—"
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<TourismContent>() {
        override fun areItemsTheSame(oldItem: TourismContent, newItem: TourismContent) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TourismContent, newItem: TourismContent) = oldItem == newItem
    }
}
