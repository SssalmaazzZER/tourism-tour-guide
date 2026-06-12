package com.example.tourismguide.presentation.tourism

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tourismguide.databinding.ItemLeaderCardBinding
import com.example.tourismguide.domain.model.ContentLeader

class LeaderAdapter : ListAdapter<ContentLeader, LeaderAdapter.ViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemLeaderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(private val binding: ItemLeaderCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(leader: ContentLeader) {
            binding.textLeaderName.text = leader.name
            binding.textLeaderRole.text = leader.role
            binding.textLeaderBio.text = leader.bio
            binding.imageLeader.load(leader.imageUrl) { crossfade(true) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<ContentLeader>() {
        override fun areItemsTheSame(oldItem: ContentLeader, newItem: ContentLeader) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: ContentLeader, newItem: ContentLeader) = oldItem == newItem
    }
}
