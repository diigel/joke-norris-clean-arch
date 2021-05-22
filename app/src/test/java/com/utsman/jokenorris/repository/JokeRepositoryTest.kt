package com.utsman.jokenorris.repository

import com.utsman.jokenorris.utils.ServicesMock
import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.data.source.JokeDataSourceImpl
import com.utsman.jokenorris.domain.Mapper
import com.utsman.jokenorris.usecase.JokeRepository
import com.utsman.jokenorris.usecase.JokeRepositoryImpl
import com.utsman.jokenorris.utils.*
import com.utsman.jokenorris.utils.mockResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class JokeRepositoryTest {

    private lateinit var repository: JokeRepository

    private val mockWebServer = MockWebServer()
    private val baseUrl = mockWebServer.url("/")
    private val services = ServicesMock.create(baseUrl)

    @Before
    fun `config repository`() {
        val dataSource = JokeDataSourceImpl(services)
        repository = JokeRepositoryImpl(dataSource)
    }

    @After
    fun `config after`() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should result category success`() = runBlocking {
        mockWebServer.mockResponse("categories.json", 200)

        repository.getCategories()
        val actualResult = repository.categories.first()
        val actual = actualResult.getDataOrNull()

        val expected = listOf(
            "animal",
            "career",
            "celebrity",
            "dev",
            "explicit",
            "fashion",
            "food",
            "history",
            "money",
            "movie",
            "music",
            "political",
            "religion",
            "science",
            "sport",
            "travel"
        )

        assert(actual == expected)
    }

    @Test
    fun `should result category failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)

        repository.getCategories()
        val actualResult = repository.categories.first()
        val resultException = actualResult.getThrowableOrNull() as HttpException?
        val actual = mapOf(
            "code" to resultException?.code(),
            "message" to resultException?.message
        )

        val expectedException = errorBody.toErrorException(404, "Client Error")
        val expected = mapOf(
            "code" to expectedException.code(),
            "message" to expectedException.message
        )

        assert(actual == expected)
    }

    @Test
    fun `should result random success`() = runBlocking {
        mockWebServer.mockResponse("random.json", 200)

        repository.getRandom()
        val actualResult = repository.random.first()
        val actual = actualResult.getDataOrNull()

        val expectedResponse = JokeResponse(
            categories = emptyList(),
            id = "UH-5pRDrT_a35IHHMRDGRw",
            value = "When life gives Chuck Norris lemons, he makes lemonade, and then goes to sunbathe on the beach.",
            url = "https://api.chucknorris.io/jokes/UH-5pRDrT_a35IHHMRDGRw"
        )

        val expected = Mapper.mapApiToEntity(expectedResponse)
        assert(actual == expected)
    }

    @Test
    fun `should result random failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)

        repository.getRandom()
        val actualResult = repository.random.first()
        val resultException = actualResult.getThrowableOrNull() as HttpException?
        val actual = mapOf(
            "code" to resultException?.code(),
            "message" to resultException?.message
        )

        val expectedException = errorBody.toErrorException(404, "Client Error")
        val expected = mapOf(
            "code" to expectedException.code(),
            "message" to expectedException.message
        )

        assert(actual == expected)
    }

    @Test
    fun `should result list success`() = runBlocking {
        mockWebServer.mockResponse("random.json", 200)

        repository.getList(0)
        val actualResult = repository.list.first()
        val actual = actualResult.getDataOrNull()
        val expectedResponse = listOf(
            JokeResponse(
                categories = emptyList(),
                id = "UH-5pRDrT_a35IHHMRDGRw",
                value = "When life gives Chuck Norris lemons, he makes lemonade, and then goes to sunbathe on the beach.",
                url = "https://api.chucknorris.io/jokes/UH-5pRDrT_a35IHHMRDGRw"
            )
        )

        val expected = expectedResponse.map { response -> Mapper.mapApiToEntity(response) }
        assert(actual == expected)
    }

    @Test
    fun `should result list failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)

        repository.getList(0)
        val actualResult = repository.list.first()
        val resultException = actualResult.getThrowableOrNull() as HttpException?
        val actual = mapOf(
            "code" to resultException?.code(),
            "message" to resultException?.message
        )

        val expectedException = errorBody.toErrorException(404, "Client Error")
        val expected = mapOf(
            "code" to expectedException.code(),
            "message" to expectedException.message
        )

        assert(actual == expected)
    }
}