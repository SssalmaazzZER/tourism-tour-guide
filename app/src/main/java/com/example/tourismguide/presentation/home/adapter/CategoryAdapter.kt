package com.example.tourismguide.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ItemCategoryPillBinding

data class CategoryItem(
    val label: String,
    val category: String?,
    val titleRes: Int,
    val iconRes: Int? = null
)

class CategoryAdapter(
    private val items: List<CategoryItem>,
    private val onSelected: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(ItemCategoryPillBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(items[position], position == selectedPosition)

    override fun getItemCount() = items.size

    inner class CategoryViewHolder(private val binding: ItemCategoryPillBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryItem, selected: Boolean) {
            binding.textCategory.text = item.label
            item.iconRes?.let { binding.imageIcon.setImageResource(it) }

            val context = binding.root.context
            val primary = ContextCompat.getColor(context, R.color.md_primary)
            val onPrimary = ContextCompat.getColor(context, R.color.md_on_primary)
            val surfaceVariant = ContextCompat.getColor(context, R.color.md_surface_variant)
            val onSurface = ContextCompat.getColor(context, R.color.md_on_surface)

            if (selected) {
                binding.root.setCardBackgroundColor(primary)
                binding.textCategory.setTextColor(onPrimary)
                binding.imageIcon.setColorFilter(onPrimary)
                binding.root.strokeWidth = 0
            } else {
                binding.root.setCardBackgroundColor(surfaceVariant)
                binding.textCategory.setTextColor(onSurface)
                binding.imageIcon.setColorFilter(primary)
                binding.root.strokeWidth = 0
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
