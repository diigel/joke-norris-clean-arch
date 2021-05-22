package com.utsman.jokenorris.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.jokenorris.usecase.JokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(private val repository: JokeRepository) : ViewModel() {
    val list = repository.list.asLiveData(viewModelScope.coroutineContext)
    val random = repository.random.asLiveData(viewModelScope.coroutineContext)
    val categories = repository.categories.asLiveData(viewModelScope.coroutineContext)
    val searchResult = repository.search.asLiveData(viewModelScope.coroutineContext)

    fun getList(size: Int = 5) = viewModelScope.launch { repository.getList(size) }
    fun getRandom(category: String = "") = viewModelScope.launch { repository.getRandom(category) }
    fun getCategories() = viewModelScope.launch { repository.getCategories() }
    fun search(query: String) = viewModelScope.launch { repository.getSearch(query) }

}