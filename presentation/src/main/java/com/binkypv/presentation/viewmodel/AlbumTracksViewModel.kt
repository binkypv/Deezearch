package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumDetailsDisplay
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class AlbumTracksViewModel(private val deezerRepository: DeezerRepository) : ViewModel() {
    private val _results = MutableLiveData<AlbumDetailsDisplay>()
    val results: LiveData<AlbumDetailsDisplay> = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    fun retrieveAlbumTracks(albumId: String) {
        viewModelScope.launch {
            _results.postValue(deezerRepository.getAlbumDetails(albumId).toDisplay())
        }
    }
}