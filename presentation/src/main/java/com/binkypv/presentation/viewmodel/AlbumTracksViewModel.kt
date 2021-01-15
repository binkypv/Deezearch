package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumDetailsDisplay
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.launch

class AlbumTracksViewModel(private val deezerRepository: DeezerRepository) : BaseViewModel() {
    private val _results = MutableLiveData<AlbumDetailsDisplay>()
    val results: LiveData<AlbumDetailsDisplay> = _results

    fun retrieveAlbumTracks(albumId: String) {
        viewModelScope.launch(exceptionHandler) {
            _results.postValue(deezerRepository.getAlbumDetails(albumId).toDisplay())
        }
    }
}