package com.binkypv.presentation.model

import com.binkypv.domain.model.AlbumDetailsModel
import com.binkypv.domain.model.TrackModel

data class AlbumDetailsDisplay(
    val coverUrl: String,
    val title: String,
    val artist: String,
    val tracklist: List<TrackDisplay>,
)

data class TrackDisplay(
    val title: String,
    val performers: String,
    val id: String
)

fun TrackModel.toDisplay() = TrackDisplay(title, performers, id)

fun AlbumDetailsModel.toDisplay() =
    AlbumDetailsDisplay(coverUrl, title, artist, tracklist.map { it.toDisplay() })