package com.binkypv.data.model

import com.binkypv.data.utils.MapperUtils
import com.binkypv.domain.model.ArtistModel
import com.binkypv.domain.model.SearchResultModel
import com.google.gson.annotations.SerializedName

data class ArtistSearchResultEntity(
    @SerializedName("data") val data: List<ArtistEntity>,
    @SerializedName("total") val total: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?,
) {
    fun toDomain() = SearchResultModel(MapperUtils.getNextIndexFromUrl(next), data.map { it.toDomain() })
}

data class ArtistEntity(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("link") val link: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("picture_small") val pictureSmall: String,
    @SerializedName("picture_medium") val pictureMedium: String,
    @SerializedName("picture_big") val pictureBig: String,
    @SerializedName("picture_xl") val pictureXL: String,
    @SerializedName("nb_album") val nbAlbum: Int,
    @SerializedName("nb_fan") val nbFan: Int,
    @SerializedName("radio") val radio: Boolean,
    @SerializedName("tracklist") val tracklist: String,
    @SerializedName("type") val type: String,
) {
    fun toDomain() = ArtistModel(name, picture, id)
}