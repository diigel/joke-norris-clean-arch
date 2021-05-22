package com.utsman.jokenorris.datasources

import com.utsman.jokenorris.utils.ServicesMock
import com.utsman.jokenorris.data.entity.JokeResponse
import com.utsman.jokenorris.data.source.JokeDataSource
import com.utsman.jokenorris.data.source.JokeDataSourceImpl
import com.utsman.jokenorris.utils.mockResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class JokeDataSourceTest {

    private val mockWebServer = MockWebServer()

    private val baseUrl = mockWebServer.url("/")
    private val services = ServicesMock.create(baseUrl)
    private lateinit var jokeDataSource: JokeDataSource

    @Before
    fun `config before`() {
        jokeDataSource = JokeDataSourceImpl(services)
    }

    @After
    fun `config after`() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should category success`() = runBlocking {
        mockWebServer.mockResponse("categories.json", 200)
        val actual = jokeDataSource.categories()
        val expectedElement = "fashion"

        assert(actual.contains(expectedElement))
    }

    @Test
    fun `should category failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)
        val actual = try {
            jokeDataSource.categories()
        } catch (e: HttpException) {
            null
        }

        val expected: List<String>? = null
        assert(actual == expected)
    }

    @Test
    fun `should random success`() = runBlocking {
        mockWebServer.mockResponse("random.json", 200)
        val actual = jokeDataSource.random()
        val expected = JokeResponse(
            categories = emptyList(),
            id = "UH-5pRDrT_a35IHHMRDGRw",
            value = "When life gives Chuck Norris lemons, he makes lemonade, and then goes to sunbathe on the beach.",
            url = "https://api.chucknorris.io/jokes/UH-5pRDrT_a35IHHMRDGRw"
        )

        assert(actual == expected)
    }

    @Test
    fun `should random failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)
        val actual = try {
            jokeDataSource.random()
        } catch (e: HttpException) {
            null
        }

        val expected: JokeResponse? = null
        assert(actual == expected)
    }

    @Test
    fun `should list success`() = runBlocking {
        mockWebServer.mockResponse("random.json", 200)
        val actual = jokeDataSource.list(0)
        val expected = listOf(
            JokeResponse(
                categories = emptyList(),
                id = "UH-5pRDrT_a35IHHMRDGRw",
                value = "When life gives Chuck Norris lemons, he makes lemonade, and then goes to sunbathe on the beach.",
                url = "https://api.chucknorris.io/jokes/UH-5pRDrT_a35IHHMRDGRw"
            )
        )

        assert(actual == expected)
    }

    @Test
    fun `should list failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)
        val actual = try {
            jokeDataSource.list(0)
        } catch (e: HttpException) {
            null
        }

        val expected: List<JokeResponse>? = null
        assert(actual == expected)
    }

    @Test
    fun `should search success`() = runBlocking {
        mockWebServer.mockResponse("joke-list.json", 200)
        val actual = jokeDataSource.search("some search")

        val actualIds = actual.result.map { j -> j.id }
        val expectedIds = listOf(
            "UH-5pRDrT_a35IHHMRDGRw",
            "MnW5pqS9TI-R55pnHGWUNw",
            "IIiKwRd-QeijiuzhD1H6eg",
            "SPv79G_bQWuEBMXyCMHynA",
            "7TOymQUdSwyr8rqLMWwCGg"
        )

        assert(actualIds == expectedIds)
    }

    @Test
    fun `should search failure`() = runBlocking {
        mockWebServer.mockResponse("error.json", 404)
        val actual = try {
            jokeDataSource.search("some search")
        } catch (e: HttpException) {
            null
        }

        val expected: List<JokeResponse>? = null
        assert(actual?.result == expected)
    }

}