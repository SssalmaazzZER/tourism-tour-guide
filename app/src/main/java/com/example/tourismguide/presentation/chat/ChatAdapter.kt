package com.example.tourismguide.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.databinding.ItemChatReceivedBinding
import com.example.tourismguide.databinding.ItemChatSentBinding
import com.example.tourismguide.domain.model.ChatMessage
import com.example.tourismguide.util.ImageUtils

class ChatAdapter(private val currentUserId: String) : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(Diff) {
    override fun getItemViewType(position: Int): Int = if (getItem(position).senderId == currentUserId) 1 else 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == 1) SentVH(ItemChatSentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else ReceivedVH(ItemChatReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is SentVH -> holder.bind(item)
            is ReceivedVH -> holder.bind(item)
        }
    }
    class SentVH(private val binding: ItemChatSentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) {
            binding.textMessage.text = msg.text
            msg.imageUrl?.let { ImageUtils.loadWithCoil(binding.imageMessage, it) }
        }
    }
    class ReceivedVH(private val binding: ItemChatReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) {
            binding.textMessage.text = msg.text
            msg.imageUrl?.let { ImageUtils.loadWithCoil(binding.imageMessage, it) }
        }
    }
    private object Diff : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) = oldItem == newItem
    }
}
