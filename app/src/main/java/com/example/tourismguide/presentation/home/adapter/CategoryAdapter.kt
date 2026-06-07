package com.example.tourismguide.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ItemCategoryPillBinding

data class CategoryItem(val label: String, val category: String?, val iconRes: Int? = null)

class CategoryAdapter(
    private val items: List<CategoryItem>,
    private val onSelected: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryPillBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition)
    }

    override fun getItemCount() = items.size

    inner class CategoryViewHolder(private val binding: ItemCategoryPillBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryItem, selected: Boolean) {
            binding.textCategory.text = item.label
            item.iconRes?.let { binding.imageIcon.setImageResource(it) }
            
            val context = binding.root.context
            if (selected) {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.rich_copper))
                binding.textCategory.setTextColor(ContextCompat.getColor(context, R.color.deep_navy))
                binding.imageIcon.setColorFilter(ContextCompat.getColor(context, R.color.deep_navy))
                binding.root.strokeColor = ContextCompat.getColor(context, R.color.rich_copper)
            } else {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_navy))
                binding.textCategory.setTextColor(ContextCompat.getColor(context, R.color.text_white))
                binding.imageIcon.setColorFilter(ContextCompat.getColor(context, R.color.rich_copper))
                binding.root.strokeColor = ContextCompat.getColor(context, R.color.rich_copper)
            }

            binding.root.setOnClickListener {
                val previous = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previous)
                notifyItemChanged(selectedPosition)
                onSelected(item)
            }
        }
    }
}
