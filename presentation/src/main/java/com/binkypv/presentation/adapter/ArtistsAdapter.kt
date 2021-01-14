package com.binkypv.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.binkypv.presentation.R
import com.binkypv.presentation.databinding.ArtistRowBinding
import com.binkypv.presentation.databinding.LoadingItemBinding
import com.binkypv.presentation.model.ArtistDisplay
import com.binkypv.presentation.model.ArtistListItem
import com.binkypv.presentation.model.ArtistLoadingItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

private const val LOADING_ITEM = 0
private const val ARTIST_ITEM = 1

class ArtistAdapter(private val onClick: (String, String) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<ArtistListItem, ArtistListItemViewHolder>(
        artistsDiffCallback) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArtistLoadingItem -> LOADING_ITEM
            else -> ARTIST_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            LOADING_ITEM -> {
                LoadingArtistViewHolder(
                    LoadingItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ArtistViewHolder(
                    ArtistRowBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: ArtistListItemViewHolder, position: Int) {
        if (holder is ArtistViewHolder) {
            holder.bind(getItem(position), onClick)
        }
    }
}

val artistsDiffCallback = object : DiffUtil.ItemCallback<ArtistListItem>() {
    override fun areItemsTheSame(
        oldItem: ArtistListItem,
        newItem: ArtistListItem,
    ) = oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(
        oldItem: ArtistListItem,
        newItem: ArtistListItem,
    ): Boolean = oldItem.areContentsTheSame(newItem)
}

class ArtistViewHolder(private val binding: ArtistRowBinding) : ArtistListItemViewHolder(binding.root) {
    fun bind(
        model: ArtistListItem,
        onClick: (String, String) -> Unit,
    ) {
        if (model is ArtistDisplay) {
            Glide.with(binding.artistImage)
                .load(if (model.imageUrl.isNullOrEmpty()) binding.artistImage.context.getDrawable(R.drawable.ic_artist_placeholder) else model.imageUrl)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.artistImage)

            binding.artistName.text = model.name

            binding.root.setOnClickListener { onClick(model.id, model.name) }
        }
    }
}

class LoadingArtistViewHolder(private val binding: LoadingItemBinding) :
    ArtistListItemViewHolder(binding.root)

sealed class ArtistListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)