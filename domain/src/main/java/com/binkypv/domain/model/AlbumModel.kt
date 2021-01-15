package com.binkypv.domain.model

data class AlbumsResultModel(
    val next: Int?,
    val albums: List<AlbumModel>
)

data class AlbumModel (
    val name: String,
    val artist: String,
    val imageUrl: String,
    val id: String
)