package com.example.tourismguide.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.GuideRequestEntity
import com.example.tourismguide.databinding.ItemGuideRequestBinding

class GuideRequestAdapter(
    private val onAccept: (GuideRequestEntity) -> Unit,
    private val onDecline: (GuideRequestEntity) -> Unit
) : ListAdapter<GuideRequestEntity, GuideRequestAdapter.VH>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemGuideRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    inner class VH(private val binding: ItemGuideRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GuideRequestEntity) {
            binding.textTitle.text = item.specialRequests
            binding.buttonAccept.setOnClickListener { onAccept(item) }
            binding.buttonDecline.setOnClickListener { onDecline(item) }
        }
    }
    private object Diff : DiffUtil.ItemCallback<GuideRequestEntity>() {
        override fun areItemsTheSame(oldItem: GuideRequestEntity, newItem: GuideRequestEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GuideRequestEntity, newItem: GuideRequestEntity) = oldItem == newItem
    }
}
