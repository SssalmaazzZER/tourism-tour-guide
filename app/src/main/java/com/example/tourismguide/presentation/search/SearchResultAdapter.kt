package com.example.tourismguide.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tourismguide.databinding.ItemSearchResultBinding
import com.example.tourismguide.domain.model.TourismContent

class SearchResultAdapter(
    private val onClick: (TourismContent) -> Unit
) : ListAdapter<TourismContent, SearchResultAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TourismContent) {
            binding.imageThumb.load(item.imageUrl) { crossfade(true) }
            binding.textTitle.text = item.title
            binding.textSubtitle.text = item.subtitle
            binding.textType.text = item.contentType
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<TourismContent>() {
        override fun areItemsTheSame(oldItem: TourismContent, newItem: TourismContent) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TourismContent, newItem: TourismContent) = oldItem == newItem
    }
}
