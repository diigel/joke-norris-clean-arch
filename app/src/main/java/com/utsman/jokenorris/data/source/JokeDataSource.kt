package com.utsman.jokenorris.data.source

import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.data.entity.JokeSearchResponse

interface JokeDataSource {
    suspend fun categories(): List<String>
    suspend fun random(category: String = ""): JokeResponse
    suspend fun list(size: Int): List<JokeResponse>
    suspend fun search(query: String): JokeSearchResponse
}