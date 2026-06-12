package com.example.tourismguide.presentation.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.databinding.ItemDiscoverCategoryBinding
import com.example.tourismguide.presentation.navigation.CategoryNavigation

class DiscoverCategoryAdapter(
    private val onClick: (CategoryNavigation.CategoryRoute) -> Unit
) : ListAdapter<DiscoverCategoryAdapter.Item, DiscoverCategoryAdapter.ViewHolder>(Diff) {

    data class Item(val route: CategoryNavigation.CategoryRoute, val iconRes: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemDiscoverCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemDiscoverCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.iconCategory.setImageResource(item.iconRes)
            binding.textCategory.setText(item.route.titleRes)
            binding.root.setOnClickListener { onClick(item.route) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.route.contentType == newItem.route.contentType
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }
}
