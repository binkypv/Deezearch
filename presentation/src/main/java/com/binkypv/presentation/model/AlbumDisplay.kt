package com.binkypv.presentation.model

import com.binkypv.domain.model.AlbumModel

data class AlbumDisplay(
    val name: String,
    val artist: String,
    val imageUrl: String,
    val id: String,
) : AlbumListItem() {
    override fun areItemsTheSame(other: AlbumListItem) =
        other is AlbumDisplay && other.id == id

    override fun areContentsTheSame(other: AlbumListItem) =
        other is AlbumDisplay && other == this
}

fun AlbumModel.toDisplay() = AlbumDisplay(name, artist, imageUrl, id)

sealed class AlbumListItem {
    abstract fun areItemsTheSame(other: AlbumListItem): Boolean
    abstract fun areContentsTheSame(other: AlbumListItem): Boolean
}

object AlbumLoadingItem : AlbumListItem() {
    override fun areItemsTheSame(other: AlbumListItem) =
        other is AlbumLoadingItem

    override fun areContentsTheSame(other: AlbumListItem) =
        other is AlbumLoadingItem
}