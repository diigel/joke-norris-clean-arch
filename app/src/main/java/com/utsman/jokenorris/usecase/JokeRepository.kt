package com.utsman.jokenorris.usecase

import com.utsman.jokenorris.domain.ResultState
import com.utsman.jokenorris.domain.entity.Joke
import kotlinx.coroutines.flow.MutableStateFlow

interface JokeRepository {
    val categories: MutableStateFlow<ResultState<List<String>>>
    val random: MutableStateFlow<ResultState<Joke>>
    val list: MutableStateFlow<ResultState<List<Joke>>>
    val search: MutableStateFlow<ResultState<List<Joke>>>

    suspend fun getCategories()
    suspend fun getRandom(category: String = "")
    suspend fun getList(size: Int = 5)
    suspend fun getSearch(query: String)
}