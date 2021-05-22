package com.utsman.jokenorris.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.utsman.jokenorris.utils.ServicesMock
import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.data.source.JokeDataSourceImpl
import com.utsman.jokenorris.domain.Mapper
import com.utsman.jokenorris.ui.viewModel.JokeViewModel
import com.utsman.jokenorris.usecase.JokeRepositoryImpl
import com.utsman.jokenorris.utils.*
import com.utsman.jokenorris.utils.mockResponse
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class JokeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var jokeViewModel: JokeViewModel

    private val mockWebServer = MockWebServer()
    private val baseUrl = mockWebServer.url("/")
    private val services = ServicesMock.create(baseUrl)

    @Before
    fun `config before`() {
        val dataSource = JokeDataSourceImpl(services)
        val repository = JokeRepositoryImpl(dataSource)
        jokeViewModel = JokeViewModel(repository)
    }

    @After
    fun `config after`() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should category success`() = runBlockingTest {
        mockWebServer.mockResponse("categories.json", 200)

        jokeViewModel.getCategories()
        val actualResult = jokeViewModel.categories.getOrAwaitValue()
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
    fun `should category failure`() = runBlockingTest {
        mockWebServer.mockResponse("error.json", 404)

        jokeViewModel.getCategories()
        val actualResult = jokeViewModel.categories.getOrAwaitValue()
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
    fun `should random success`() = runBlockingTest {
        mockWebServer.mockResponse("random.json", 200)

        jokeViewModel.getRandom()
        val result = jokeViewModel.random.getOrAwaitValue()

        val actual = result.getDataOrNull()
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
    fun `should random failure`() = runBlockingTest {
        mockWebServer.mockResponse("error.json", 404)

        jokeViewModel.getRandom()
        val actualResult = jokeViewModel.random.getOrAwaitValue()
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
    fun `should list success`() = runBlockingTest {
        mockWebServer.mockResponse("random.json", 200)

        jokeViewModel.getList(0)
        val actualResult = jokeViewModel.list.getOrAwaitValue()
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
    fun `should list failure`() = runBlockingTest {
        mockWebServer.mockResponse("error.json", 404)

        jokeViewModel.getList(0)
        val actualResult = jokeViewModel.list.getOrAwaitValue()
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