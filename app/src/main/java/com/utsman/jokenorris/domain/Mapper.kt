package com.utsman.jokenorris.domain

import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.domain.entity.Joke
import com.utsman.jokenorris.utils.onSuccess
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object Mapper {

    fun mapApiToEntity(jokeResponse: JokeResponse): Joke {
        return Joke(joke = jokeResponse.value)
    }

    suspend inline fun <T : Any> mapResultToData(resultState: ResultState<T>): ResultState<T>? =
        suspendCancellableCoroutine { task ->
            resultState.onSuccess {
                val data = ResultState.Success(it)
                try {
                    task.resume(data)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    task.resume(null)
                }
            }
        }

    inline fun <T: Any, U: Any> mapResult(resultState: ResultState<out T>, mapper: T.() -> U): ResultState<U> {
        return when (resultState) {
            is ResultState.Success -> {
                val data = resultState.data
                val mapData = mapper.invoke(data)
                ResultState.Success(mapData)
            }
            is ResultState.Idle -> ResultState.Idle()
            is ResultState.Error -> ResultState.Error(resultState.throwable)
            is ResultState.Loading -> ResultState.Loading()
        }
    }
}