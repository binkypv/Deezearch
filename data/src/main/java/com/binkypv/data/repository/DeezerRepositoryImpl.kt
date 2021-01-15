package com.binkypv.data.repository

import com.binkypv.data.datasource.DeezerDataSource
import com.binkypv.domain.model.AlbumDetailsModel
import com.binkypv.domain.model.AlbumsResultModel
import com.binkypv.domain.model.SearchResultModel
import com.binkypv.domain.repository.DeezerRepository

class DeezerRepositoryImpl(private val deezerDataSource: DeezerDataSource) : DeezerRepository {
    override suspend fun getArtists(term: String, index: Int?): SearchResultModel =
        deezerDataSource.getArtists(term, index).toDomain()

    override suspend fun getAlbums(
        artistId: String,
        artistName: String,
        index: Int?,
    ): AlbumsResultModel =
        deezerDataSource.getArtistAlbums(artistId, index).toDomain(artistName)

    override suspend fun getAlbumDetails(albumId: String): AlbumDetailsModel =
        deezerDataSource.getAlbumDetails(albumId).toDomain()
}