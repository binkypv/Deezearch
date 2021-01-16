package com.binkypv.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binkypv.presentation.viewmodel.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {
    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String> = _error

    protected val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }
}