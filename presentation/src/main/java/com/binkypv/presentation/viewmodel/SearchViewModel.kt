package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.ArtistListItem
import com.binkypv.presentation.model.ArtistLoadingItem
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchViewModel(private val deezerRepository: DeezerRepository) : ViewModel() {

    private val _results = MutableLiveData<List<ArtistListItem>>()
    val results: LiveData<List<ArtistListItem>> = _results

    private val _next = MutableLiveData<Int?>()
    val next: LiveData<Int?> = _next

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    var fetching = false
    var searchTerm: String? = null

    fun retrieveArtists(term: String, index: Int? = null) {
        searchTerm = term
        viewModelScope.launch {
            _loading.postValue(Unit)

            deezerRepository.getArtists(term, index).apply {
                val fetchedArtistList: MutableList<ArtistListItem> =
                    artists.map { it.toDisplay() }.toMutableList()
                if (next != null) fetchedArtistList.add(ArtistLoadingItem)
                _results.postValue(fetchedArtistList)
                _next.postValue(next)
            }
        }
    }

    fun loadMore() {
        if (!fetching && !searchTerm.isNullOrBlank()) {
            fetching = true
            viewModelScope.launch {
                _loading.postValue(Unit)

                deezerRepository.getArtists(searchTerm ?: "", _next.value).apply {
                    val fetchedArtistList: MutableList<ArtistListItem> =
                        artists.map { it.toDisplay() }.toMutableList()
                    if (next != null) fetchedArtistList.add(ArtistLoadingItem)

                    val currentList =
                        _results.value?.toMutableList()?.apply { remove(ArtistLoadingItem) }
                            ?: mutableListOf()
                    currentList.addAll(fetchedArtistList)
                    _results.postValue(currentList)

                    _next.postValue(next)
                    fetching = false
                }
            }
        }
    }
}