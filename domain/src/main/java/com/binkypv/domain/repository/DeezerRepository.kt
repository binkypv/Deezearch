package com.binkypv.domain.repository

import com.binkypv.domain.model.SearchResultModel

interface DeezerRepository {
    suspend fun getArtists(term: String): SearchResultModel
}