package com.binkypv.data.datasource

import com.binkypv.data.model.ArtistEntity
import com.binkypv.data.model.ArtistSearchResultEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerDataSource {

    @GET("/search/artist")
    suspend fun getArtists(@Query("q") term: String): ArtistSearchResultEntity

}