package com.binkypv.domain.model

data class AlbumDetailsModel(
    val coverUrl: String,
    val title: String,
    val artist: String,
    val tracklist: List<TrackModel>
)

data class TrackModel(
    val title: String,
    val performers: String,
    val id: String
)