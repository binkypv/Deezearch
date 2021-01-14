package com.binkypv.presentation.model

import com.binkypv.domain.model.AlbumModel

data class AlbumDisplay(
    val name: String,
    val artist: String,
    val imageUrl: String,
    val id: String
)

fun AlbumModel.toDisplay() = AlbumDisplay(name, artist, imageUrl, id)