package com.example.tourismguide.presentation.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.R
import com.example.tourismguide.data.local.entity.GuideEntity
import com.example.tourismguide.databinding.ItemGuideCardBinding
import com.example.tourismguide.util.ImageUtils
import java.util.Locale

class GuideAdapter(
    private val onOpen: (GuideEntity) -> Unit,
    private val onHire: (GuideEntity) -> Unit
) : ListAdapter<GuideEntity, GuideAdapter.GuideViewHolder>(Diff) {
    
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder =
        GuideViewHolder(ItemGuideCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
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

    inner class GuideViewHolder(private val binding: ItemGuideCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(guide: GuideEntity) {
            ImageUtils.loadWithCoil(binding.imageAvatar, guide.avatarUrl)
            binding.textName.text = guide.name
            binding.textRating.text = String.format(Locale.US, "%.1f (120 avis)", guide.rating)
            binding.textLanguages.text = "🇲🇦 🇫🇷 🇬🇧" // Sample languages
            binding.textPrice.text = binding.root.context.getString(R.string.price_per_hour, guide.pricePerHour)
            
            // Availability animation (pulsating ring)
            val pulse = AnimationUtils.loadAnimation(binding.root.context, R.anim.pulse)
            binding.availabilityRing.startAnimation(pulse)

            binding.buttonHire.setOnClickListener { onHire(guide) }
            binding.root.setOnClickListener { onOpen(guide) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<GuideEntity>() {
        override fun areItemsTheSame(oldItem: GuideEntity, newItem: GuideEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GuideEntity, newItem: GuideEntity) = oldItem == newItem
    }
}
