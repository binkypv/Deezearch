package com.binkypv.domain.model

data class SearchResultModel(
    val next: String?,
    val artists: List<ArtistModel>
)

data class ArtistModel(
    val name: String,
    val imageUrl: String,
    val id: String
)