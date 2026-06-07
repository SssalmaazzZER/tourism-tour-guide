package com.example.tourismguide.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tourismguide.databinding.ItemHomeSectionBinding
import com.example.tourismguide.domain.model.Place
import com.example.tourismguide.presentation.home.HomeSection

class SectionAdapter(
    private val onPlaceClick: (Place, View) -> Unit
) : ListAdapter<HomeSection, SectionAdapter.SectionViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder =
        SectionViewHolder(ItemHomeSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) = holder.bind(getItem(position))

    inner class SectionViewHolder(private val binding: ItemHomeSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        private val placeAdapter = PlaceCardAdapter(onPlaceClick)

        init {
            binding.placeRecycler.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.placeRecycler.adapter = placeAdapter
            // Removing horizontal layout for a more premium vertical list as per "Sunset Sahara" Grid/Vertical RecyclerView requirement
            binding.placeRecycler.isNestedScrollingEnabled = false
        }

        fun bind(section: HomeSection) {
            binding.textSectionTitle.text = section.title
            placeAdapter.submitList(section.places)
        }
    }

    private object Diff : DiffUtil.ItemCallback<HomeSection>() {
        override fun areItemsTheSame(oldItem: HomeSection, newItem: HomeSection) = oldItem.title == newItem.title
        override fun areContentsTheSame(oldItem: HomeSection, newItem: HomeSection) = oldItem == newItem
    }
}
