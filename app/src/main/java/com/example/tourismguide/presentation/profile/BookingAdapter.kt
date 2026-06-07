package com.example.tourismguide.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.BookingEntity
import com.example.tourismguide.databinding.ItemBookingBinding

class BookingAdapter : ListAdapter<BookingEntity, BookingAdapter.VH>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    class VH(private val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookingEntity) { binding.textTitle.text = item.status }
    }
    private object Diff : DiffUtil.ItemCallback<BookingEntity>() {
        override fun areItemsTheSame(oldItem: BookingEntity, newItem: BookingEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BookingEntity, newItem: BookingEntity) = oldItem == newItem
    }
}
