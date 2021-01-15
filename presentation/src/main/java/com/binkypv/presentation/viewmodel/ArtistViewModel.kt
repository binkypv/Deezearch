package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.binkypv.domain.repository.DeezerRepository
import com.binkypv.presentation.model.AlbumListItem
import com.binkypv.presentation.model.AlbumLoadingItem
import com.binkypv.presentation.model.toDisplay
import kotlinx.coroutines.launch

class ArtistViewModel(private val deezerRepository: DeezerRepository) : BaseViewModel() {
    private val _results = MutableLiveData<List<AlbumListItem>>()
    val results: LiveData<List<AlbumListItem>> = _results

    private val _next = MutableLiveData<Int?>()
    val next: LiveData<Int?> = _next

    private var fetching = false

    fun retrieveAlbums(artistId: String, artistName: String) {
        viewModelScope.launch(exceptionHandler) {
            deezerRepository.getAlbums(artistId, artistName).apply {
                val fetchedAlbumsList: MutableList<AlbumListItem> =
                    albums.map { it.toDisplay() }.toMutableList()
                _results.postValue(albums.map { it.toDisplay() })
                if (next != null) fetchedAlbumsList.add(AlbumLoadingItem)
                _results.postValue(fetchedAlbumsList)
                _next.postValue(next)
            }
        }
    }

    fun loadMore(artistId: String, artistName: String) {
        if (!fetching) {
            fetching = true
            viewModelScope.launch(exceptionHandler) {
                deezerRepository.getAlbums(artistId, artistName, _next.value).apply {
                    val fetchedAlbumsList: MutableList<AlbumListItem> =
                        albums.map { it.toDisplay() }.toMutableList()
                    if (next != null) fetchedAlbumsList.add(AlbumLoadingItem)

                    val currentList =
                        _results.value?.toMutableList()?.apply { remove(AlbumLoadingItem) }
                            ?: mutableListOf()
                    currentList.addAll(fetchedAlbumsList)
                    _results.postValue(currentList)

                    _next.postValue(next)
                    fetching = false
                }
            }
        }
    }
}