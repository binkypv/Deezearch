package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumDisplay
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.launch

class ArtistViewModel(private val deezerRepository: DeezerRepository) : BaseViewModel() {
    private val _results = MutableLiveData<List<AlbumDisplay>>()
    val results: LiveData<List<AlbumDisplay>> = _results

    private val _next = MutableLiveData<Int?>()
    val next: LiveData<Int?> = _next

    fun retrieveAlbums(artistId: String, artistName: String) {
        viewModelScope.launch(exceptionHandler) {
            _results.postValue(deezerRepository.getAlbums(artistId, artistName)
                .map { it.toDisplay() })
        }
    }
}