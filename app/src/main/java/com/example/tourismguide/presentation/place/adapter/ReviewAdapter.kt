package com.example.tourismguide.presentation.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.databinding.ItemReviewBinding
import java.text.DateFormat
import java.util.Date

class ReviewAdapter : ListAdapter<ReviewEntity, ReviewAdapter.ReviewViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) = holder.bind(getItem(position))

    class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewEntity) {
            val author = review.authorId.ifBlank { "Traveler" }
            binding.textInitials.text = author.take(2).uppercase()
            binding.textAuthor.text = author
            binding.ratingBar.rating = review.stars.toFloat()
            binding.textReview.text = review.text
            binding.textDate.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(review.createdAt))
        }
    }

    private object Diff : DiffUtil.ItemCallback<ReviewEntity>() {
        override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity) = oldItem == newItem
    }
}
