package com.utsman.jokenorris.data

import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.data.entity.JokeSearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("/jokes/categories")
    suspend fun categories(): List<String>

    @GET("/jokes/random")
    suspend fun random(): JokeResponse

    @GET("/jokes/random")
    suspend fun randomFromCategories(
        @Query("category") categories: String
    ): JokeResponse

    @GET("/jokes/search")
    suspend fun search(
        @Query("query") search: String
    ): JokeSearchResponse

    companion object {
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private fun provideOkHttp() = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        fun create(baseUrl: String): Services = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttp())
            .build()
            .create(Services::class.java)
    }
}