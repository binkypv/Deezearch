package com.binkypv.data.model

import com.binkypv.domain.model.AlbumModel
import com.google.gson.annotations.SerializedName

data class ArtistAlbumsEntity(
    @SerializedName("data") val data: List<AlbumEntity>
)

data class AlbumEntity (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("cover") val cover: String,
    @SerializedName("cover_small") val coverSmall: String,
    @SerializedName("cover_medium") val coverMedium: String,
    @SerializedName("cover_big") val coverBig: String,
    @SerializedName("cover_xl") val coverXl: String,
    @SerializedName("md5image") val md5image: String,
    @SerializedName("genre_id") val genreId: String,
    @SerializedName("fans") val fans: Int,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("tracklist") val tracklist: String,
    @SerializedName("explicity_lyrics") val explicitLyrics: Boolean,
    @SerializedName("type") val type: String
){
    fun toDomain(artist: String) = AlbumModel(title, artist, cover, id)
}