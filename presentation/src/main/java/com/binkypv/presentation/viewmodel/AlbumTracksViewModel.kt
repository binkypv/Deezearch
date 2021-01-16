package com.binkypv.presentation.viewmodel

import androidx.lifecycle.*
import com.binkypv.domain.model.AlbumDetailsModel
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumDetailsDisplay
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.launch

class AlbumTracksViewModel(private val deezerRepository: DeezerRepository) : BaseViewModel() {
    private val _results = MutableLiveData<AlbumDetailsModel>()
    val results: LiveData<AlbumDetailsDisplay> = _results.switchMap { result ->
        liveData { emit(result.toDisplay()) }
    }

    fun start(albumId: String) {
        viewModelScope.launch(exceptionHandler) {
            _results.postValue(deezerRepository.getAlbumDetails(albumId))
        }
    }
}