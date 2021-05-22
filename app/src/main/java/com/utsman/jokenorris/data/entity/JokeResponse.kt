package com.utsman.jokenorris.data.entity

data class JokeResponse(
    val categories: List<String>,
    val id: String,
    val value: String,
    val url: String
)