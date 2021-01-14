package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumDisplay
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ArtistViewModel(private val deezerRepository: DeezerRepository) : ViewModel() {
    private val _results = MutableLiveData<List<AlbumDisplay>>()
    val results: LiveData<List<AlbumDisplay>> = _results

    private val _next = MutableLiveData<Int?>()
    val next: LiveData<Int?> = _next

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    fun retrieveAlbums(artistId: String, artistName: String) {
        viewModelScope.launch {
            _results.postValue(deezerRepository.getAlbums(artistId, artistName)
                .map { it.toDisplay() })
        }
    }
}