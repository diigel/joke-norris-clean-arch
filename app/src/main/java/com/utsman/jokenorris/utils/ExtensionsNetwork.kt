package com.utsman.jokenorris.utils

import androidx.annotation.VisibleForTesting
import com.utsman.jokenorris.domain.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

suspend fun <T: Any> fetch(call: suspend () -> T): Flow<ResultState<T>> = flow {
    emit(ResultState.Loading())
    try {
        emit(ResultState.Success(data = call.invoke()))
    } catch (e: Throwable) {
        emit(ResultState.Error<T>(throwable = e))
    }
}

fun <T: Any>ResultState<T>.getThrowableOrNull(): Throwable? {
    return if (this is ResultState.Error) {
        return this.throwable
    } else {
        null
    }
}

fun <T: Any>ResultState<T>.getDataOrNull(): T? {
    return if (this is ResultState.Success) {
        return this.data
    } else {
        null
    }
}

fun <T: Any>idle(): MutableStateFlow<ResultState<T>> = run {
    MutableStateFlow(ResultState.Idle())
}

fun <T: Any> ResultState<T>.onSuccess(result: (T) -> Unit) {
    if (this is ResultState.Success) {
        result.invoke(this.data)
    }
}

fun <T: Any> ResultState<T>.onFailure(result: (Throwable) -> Unit) {
    if (this is ResultState.Error) {
        result.invoke(this.throwable)
    }
}

fun <T: Any> ResultState<T>.onIdle(result: () -> Unit) {
    if (this is ResultState.Idle) {
        result.invoke()
    }
}

fun <T: Any> ResultState<T>.onLoading(result: () -> Unit) {
    if (this is ResultState.Loading) {
        result.invoke()
    }
}
