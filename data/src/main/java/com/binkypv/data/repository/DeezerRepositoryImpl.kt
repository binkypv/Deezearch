package com.binkypv.data.repository

import com.binkypv.data.datasource.DeezerDataSource
import com.binkypv.domain.model.SearchResultModel
import com.binkypv.domain.repository.DeezerRepository

class DeezerRepositoryImpl(private val deezerDataSource: DeezerDataSource) : DeezerRepository {
    override suspend fun getArtists(term: String): SearchResultModel =
        deezerDataSource.getArtists(term).toDomain()
}