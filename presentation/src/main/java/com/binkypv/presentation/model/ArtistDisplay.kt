package com.binkypv.presentation.model

import com.binkypv.domain.model.ArtistModel

data class ArtistDisplay(
    val name: String,
    val imageUrl: String,
    val id: String
)

fun ArtistModel.toDisplay() = ArtistDisplay(name, imageUrl, id)