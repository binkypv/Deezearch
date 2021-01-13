package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.ArtistDisplay
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchViewModel(private val deezerRepository: DeezerRepository): ViewModel() {

    private val _results = MutableLiveData<List<ArtistDisplay>>()
    val results: LiveData<List<ArtistDisplay>> = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val _next = MutableLiveData<String>()
    val next: LiveData<String> = _next

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    fun retrieveArtists(term: String){
        viewModelScope.launch {
            _loading.postValue(Unit)
            deezerRepository.getArtists(term).apply {
                _results.postValue(artists.map { it.toDisplay() })
                _next.postValue(next)
            }
        }
    }
}