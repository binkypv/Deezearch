package com.binkypv.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.binkypv.presentation.R
import com.binkypv.presentation.databinding.AlbumRowBinding
import com.binkypv.presentation.databinding.LoadingItemBinding
import com.binkypv.presentation.model.AlbumDisplay
import com.binkypv.presentation.model.AlbumListItem
import com.binkypv.presentation.model.AlbumLoadingItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

private const val LOADING_ITEM = 0
private const val ALBUM_ITEM = 1

class AlbumsAdapter(private val onClick: (String) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<AlbumListItem, AlbumListItemViewHolder>(
        albumsDiffCallback) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlbumLoadingItem -> LOADING_ITEM
            else -> ALBUM_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            LOADING_ITEM -> {
                LoadingAlbumViewHolder(
                    LoadingItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                AlbumViewHolder(
                    AlbumRowBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: AlbumListItemViewHolder, position: Int) {
        if (holder is AlbumViewHolder) {
            holder.bind(getItem(position), onClick)
        }
    }
}

val albumsDiffCallback = object : DiffUtil.ItemCallback<AlbumListItem>() {
    override fun areItemsTheSame(
        oldItem: AlbumListItem,
        newItem: AlbumListItem,
    ) = oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(
        oldItem: AlbumListItem,
        newItem: AlbumListItem,
    ): Boolean = oldItem.areContentsTheSame(newItem)
}

class AlbumViewHolder(private val binding: AlbumRowBinding) :
    AlbumListItemViewHolder(binding.root) {
    fun bind(
        model: AlbumListItem,
        onClick: (String) -> Unit,
    ) {
        if (model is AlbumDisplay) {
            Glide.with(binding.artistAlbumImage)
                .load(if (model.imageUrl.isEmpty()) binding.artistAlbumImage.context.getDrawable(R.drawable.ic_artist_placeholder) else model.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.artistAlbumImage)

            binding.artistAlbumTitle.text = model.name
            binding.artistAlbumPerformer.text = model.artist

            binding.root.setOnClickListener { onClick(model.id) }
        }
    }
}

class LoadingAlbumViewHolder(private val binding: LoadingItemBinding) :
    AlbumListItemViewHolder(binding.root)

sealed class AlbumListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)