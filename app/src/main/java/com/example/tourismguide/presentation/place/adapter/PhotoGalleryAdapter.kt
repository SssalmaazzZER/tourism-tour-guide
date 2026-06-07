package com.example.tourismguide.presentation.place.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.data.local.entity.UserPhotoEntity
import com.example.tourismguide.databinding.ItemPhotoThumbnailBinding
import com.example.tourismguide.util.ImageUtils

class PhotoGalleryAdapter : ListAdapter<UserPhotoEntity, PhotoGalleryAdapter.PhotoViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(ItemPhotoThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) = holder.bind(getItem(position))

    class PhotoViewHolder(private val binding: ItemPhotoThumbnailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: UserPhotoEntity) {
            val url = photo.remoteUrl ?: photo.localPath
            ImageUtils.loadWithCoil(binding.imagePhoto, url)
            binding.root.setOnClickListener {
                Dialog(binding.root.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
                    val image = ImageView(context).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        ImageUtils.loadWithCoil(this, url)
                        setOnClickListener { dismiss() }
                    }
                    setContentView(image)
                    show()
                }
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<UserPhotoEntity>() {
        override fun areItemsTheSame(oldItem: UserPhotoEntity, newItem: UserPhotoEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: UserPhotoEntity, newItem: UserPhotoEntity) = oldItem == newItem
    }
}
