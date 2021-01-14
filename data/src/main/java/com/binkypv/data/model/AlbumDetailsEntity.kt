package com.binkypv.data.model

import com.binkypv.domain.model.AlbumDetailsModel
import com.binkypv.domain.model.TrackModel
import com.google.gson.annotations.SerializedName

data class AlbumDetailsEntity(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("upc") val upc: String,
    @SerializedName("link") val link: String,
    @SerializedName("share") val share: String,
    @SerializedName("cover") val cover: String,
    @SerializedName("cover_small") val coverSmall: String,
    @SerializedName("cover_medium") val coverMedium: String,
    @SerializedName("cover_big") val coverBig: String,
    @SerializedName("cover_xl") val coverXL: String,
    @SerializedName("md5_image") val md5image: String,
    @SerializedName("genre_id") val genreId: String,
    @SerializedName("genres") val genres: GenresWrapperEntity,
    @SerializedName("label") val label: String,
    @SerializedName("nb_tracks") val nbTracks: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("fans") val fans: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("available") val available: Boolean,
    @SerializedName("tracklist") val tracklist: String,
    @SerializedName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerializedName("explicit_content_cover") val explicitContentCover: Int,
    @SerializedName("contributors") val contributors: List<ContributorEntity>,
    @SerializedName("artist") val artist: AlbumArtistEntity,
    @SerializedName("type") val type: String,
    @SerializedName("tracks") val tracks: TracksWrapperEntity
){
    fun toDomain() = AlbumDetailsModel(coverBig,title,artist.name,tracks.data.map{it.toDomain()})
}

data class GenresWrapperEntity(
    @SerializedName("data") val data: List<GenreEntity>
)

data class GenreEntity(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("type") val type: String
)

data class ContributorEntity(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("link") val link: String,
    @SerializedName("share") val share: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("picture_small") val pictureSmall: String,
    @SerializedName("picture_medium") val pictureMedium: String,
    @SerializedName("picture_big") val pictureBig: String,
    @SerializedName("picture_xl") val pictureXL: String,
    @SerializedName("radio") val radio: Boolean,
    @SerializedName("tracklist") val tracklist: String,
    @SerializedName("type") val type: String,
    @SerializedName("role") val role: String
)

data class TracksWrapperEntity(
    @SerializedName("data") val data: List<TrackEntity>
)

data class TrackEntity(
    @SerializedName("id") val id: String,
    @SerializedName("readable") val readable: Boolean,
    @SerializedName("title") val title: String,
    @SerializedName("title_short") val titleShort: String,
    @SerializedName("title_version") val titleVersion: String,
    @SerializedName("link") val link: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("rank") val rank: Int,
    @SerializedName("explicit_lyrics") val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics") val explicitContentLyrics: Int,
    @SerializedName("explicit_content_cover") val explicitContentCover: Int,
    @SerializedName("preview") val preview: String,
    @SerializedName("md5_image") val md5image: String,
    @SerializedName("type") val type: String,
    @SerializedName("artist") val artist: TrackArtistEntity
){
    fun toDomain() = TrackModel(title, artist.name, id)
}

data class TrackArtistEntity(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("tracklist") val tracklist: String,
    @SerializedName("type") val type: String
)

data class AlbumArtistEntity(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("picture_small") val pictureSmall: String,
    @SerializedName("picture_medium") val pictureMedium: String,
    @SerializedName("picture_big") val pictureBig: String,
    @SerializedName("picture_xl") val pictureXL: String,
    @SerializedName("tracklist") val tracklist: String,
    @SerializedName("type") val type: String
)