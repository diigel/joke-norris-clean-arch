package com.utsman.jokenorris.data.source

import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.data.Services
import com.utsman.jokenorris.data.entity.JokeSearchResponse
import com.utsman.jokenorris.utils.logD
import kotlinx.coroutines.delay
import javax.inject.Inject

class JokeDataSourceImpl @Inject constructor(private val services: Services) : JokeDataSource {
    override suspend fun categories(): List<String> {
        return services.categories()
    }

    override suspend fun random(category: String): JokeResponse {
        return if (category.isEmpty()) {
            services.random()
        } else {
            services.randomFromCategories(category)
        }
    }

    override suspend fun list(size: Int): List<JokeResponse> {
        val listSize = 0..size
        return listSize.map {
            services.random()
        }
    }

    override suspend fun search(query: String): JokeSearchResponse {
        return services.search(query)
    }
}