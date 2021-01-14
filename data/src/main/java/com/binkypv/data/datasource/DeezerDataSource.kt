package com.binkypv.data.datasource

import com.binkypv.data.model.AlbumEntity
import com.binkypv.data.model.ArtistAlbumsEntity
import com.binkypv.data.model.ArtistSearchResultEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerDataSource {

    @GET("/search/artist")
    suspend fun getArtists(
        @Query("q") term: String,
        @Query("index") index: Int? = null,
    ): ArtistSearchResultEntity

    @GET("/artist/{artistId}/albums")
    suspend fun getArtistAlbums(
        @Path("artistId") artistId: String
    ): ArtistAlbumsEntity

}