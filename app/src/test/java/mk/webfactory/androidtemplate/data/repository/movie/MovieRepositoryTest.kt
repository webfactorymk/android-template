package mk.webfactory.androidtemplate.data.repository.movie

import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.data.repository.movie.MovieCacheDataSource
import mk.webfactory.template.data.repository.movie.MovieRemoteDataSource
import mk.webfactory.template.data.repository.movie.MovieRepository
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.network.api.MovieApiService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

/**
 * Tests for [MovieRepository]
 */
@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    private val mockMovie1 = mock(Movie::class.java)
    private val itemsAtPage1 = listOf<Movie>(mockMovie1)
    private val page1Response = PaginatedResponse(1, 10, itemsAtPage1)

    @Mock
    lateinit var movieApiService: MovieApiService
    lateinit var movieCacheDataSource: MovieCacheDataSource
    lateinit var movieRepository: MovieRepository

    @Before
    fun setUp() {
        movieCacheDataSource = spy(MovieCacheDataSource("userId"))

        movieRepository = MovieRepository(
            "userId",
            MovieRemoteDataSource("userId", movieApiService),
            movieCacheDataSource
        )

        Mockito.`when`(movieApiService.getPopularMovies(1)).thenReturn(Single.just(page1Response))
    }

    @Test
    fun getMoviesAndSaveInCache() {
        val retrievedItems = movieRepository.getPopularMovies(1).blockingGet()

        assertEquals(itemsAtPage1, retrievedItems)
        verify(movieApiService, times(1)).getPopularMovies(1)
        verify(movieCacheDataSource, times(1)).setPopularMovies(1, itemsAtPage1)
    }

    @Test
    fun getSubsequentCachedMovies() {
        val retrievedItems = movieRepository.getPopularMovies(1)
            .ignoreElement()
            .andThen(movieRepository.getPopularMovies(1))
            .blockingGet()

        assertEquals(itemsAtPage1, retrievedItems)

        verify(movieApiService, times(1)).getPopularMovies(1)
        verify(movieCacheDataSource, times(2)).getPopularMovies(1)
        verify(movieCacheDataSource, times(1)).setPopularMovies(1, itemsAtPage1)
    }
}