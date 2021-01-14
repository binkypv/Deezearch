package com.binkypv.domain.repository

import com.binkypv.domain.model.AlbumDetailsModel
import com.binkypv.domain.model.AlbumModel
import com.binkypv.domain.model.SearchResultModel

interface DeezerRepository {
    suspend fun getArtists(term: String, index: Int?): SearchResultModel
    suspend fun getAlbums(artistId: String, artistName: String): List<AlbumModel>
    suspend fun getAlbumDetails(albumId: String): AlbumDetailsModel
}