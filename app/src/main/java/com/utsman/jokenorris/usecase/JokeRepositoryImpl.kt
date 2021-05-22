package com.utsman.jokenorris.usecase

import com.utsman.jokenorris.domain.Mapper
import com.utsman.jokenorris.domain.ResultState
import com.utsman.jokenorris.domain.entity.Joke
import com.utsman.jokenorris.data.source.JokeDataSource
import com.utsman.jokenorris.utils.fetch
import com.utsman.jokenorris.utils.idle
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class JokeRepositoryImpl @Inject constructor(private val dataSource: JokeDataSource) : JokeRepository {
    override val categories: MutableStateFlow<ResultState<List<String>>> = idle()
    override val random: MutableStateFlow<ResultState<Joke>> = idle()
    override val list: MutableStateFlow<ResultState<List<Joke>>> = idle()
    override val search: MutableStateFlow<ResultState<List<Joke>>> = idle()

    override suspend fun getCategories() {
        fetch {
            dataSource.categories()
        }.collect {
            categories.value = it
        }
    }

    override suspend fun getRandom(category: String) {
        fetch {
            dataSource.random(category)
        }.map {
            Mapper.mapResult(it) { Mapper.mapApiToEntity(this) }
        }.collect {
            random.value = it
        }
    }

    override suspend fun getList(size: Int) {
        fetch {
            dataSource.list(size).distinctBy { it.value }
        }.map {
            Mapper.mapResult(it) {
                map { response ->
                    Mapper.mapApiToEntity(response)
                }
            }
        }.collect {
            list.value = it
        }
    }

    override suspend fun getSearch(query: String) {
        fetch {
            dataSource.search(query)
        }.map {
            Mapper.mapResult(it) {
                result.map { joke ->
                    Mapper.mapApiToEntity(joke)
                }
            }
        }.collect {
            search.value = it
        }
    }
}