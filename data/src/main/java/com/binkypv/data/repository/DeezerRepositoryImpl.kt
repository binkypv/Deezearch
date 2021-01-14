package com.binkypv.data.repository

import com.binkypv.data.datasource.DeezerDataSource
import com.binkypv.domain.model.AlbumModel
import com.binkypv.domain.model.SearchResultModel
import com.binkypv.domain.repository.DeezerRepository

class DeezerRepositoryImpl(private val deezerDataSource: DeezerDataSource) : DeezerRepository {
    override suspend fun getArtists(term: String, index: Int?): SearchResultModel =
        deezerDataSource.getArtists(term, index).toDomain()

    override suspend fun getAlbums(artistId: String, artistName: String): List<AlbumModel> =
        deezerDataSource.getArtistAlbums(artistId).data.map { it.toDomain(artistName) }
}