package com.binkypv.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.binkypv.presentation.R
import com.binkypv.presentation.databinding.AlbumRowBinding
import com.binkypv.presentation.model.AlbumDisplay
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AlbumsAdapter(private val onClick: (String) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<AlbumDisplay, AlbumViewHolder>(
        albumsDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AlbumViewHolder(
            AlbumRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}

val albumsDiffCallback = object : DiffUtil.ItemCallback<AlbumDisplay>() {
    override fun areItemsTheSame(
        oldItem: AlbumDisplay,
        newItem: AlbumDisplay,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: AlbumDisplay,
        newItem: AlbumDisplay,
    ): Boolean = oldItem == newItem
}

class AlbumViewHolder(private val binding: AlbumRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        model: AlbumDisplay,
        onClick: (String) -> Unit,
    ) {
        Glide.with(binding.artistAlbumImage)
            .load(if (model.imageUrl.isNullOrEmpty()) binding.artistAlbumImage.context.getDrawable(R.drawable.ic_artist_placeholder) else model.imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.artistAlbumImage)

        binding.artistAlbumTitle.text = model.name
        binding.artistAlbumPerformer.text = model.artist

        binding.root.setOnClickListener { onClick(model.id) }
    }
}