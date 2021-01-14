package com.binkypv.presentation.model

import com.binkypv.domain.model.ArtistModel

data class ArtistDisplay(
    val name: String,
    val imageUrl: String,
    val id: String,
) : ArtistListItem() {
    override fun areItemsTheSame(other: ArtistListItem) =
        other is ArtistDisplay && other.id == id

    override fun areContentsTheSame(other: ArtistListItem) =
        other is ArtistDisplay && other == this
}

fun ArtistModel.toDisplay() = ArtistDisplay(name, imageUrl, id)

sealed class ArtistListItem {
    abstract fun areItemsTheSame(other: ArtistListItem): Boolean
    abstract fun areContentsTheSame(other: ArtistListItem): Boolean
}

object ArtistLoadingItem : ArtistListItem() {
    override fun areItemsTheSame(other: ArtistListItem) =
        other is ArtistLoadingItem

    override fun areContentsTheSame(other: ArtistListItem) =
        other is ArtistLoadingItem
}