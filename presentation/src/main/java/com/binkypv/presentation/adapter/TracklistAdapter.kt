package com.binkypv.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.binkypv.presentation.databinding.TrackRowBinding
import com.binkypv.presentation.model.TrackDisplay

class TracklistAdapter() :
    androidx.recyclerview.widget.ListAdapter<TrackDisplay, TrackViewHolder>(
        trackDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TrackViewHolder(
            TrackRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

val trackDiffCallback = object : DiffUtil.ItemCallback<TrackDisplay>() {
    override fun areItemsTheSame(
        oldItem: TrackDisplay,
        newItem: TrackDisplay,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TrackDisplay,
        newItem: TrackDisplay,
    ): Boolean = oldItem == newItem
}

class TrackViewHolder(private val binding: TrackRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        model: TrackDisplay,
        position: Int
    ) {
        binding.trackNumber.text = (position + 1).toString()
        binding.trackTitle.text = model.title
        binding.trackArtist.text = model.performers
    }
}