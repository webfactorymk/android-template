package mk.webfactory.androidtemplate.data.repository.movie

import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.data.repository.movie.MovieRemoteDataSource
import mk.webfactory.template.data.repository.movie.MovieRepository
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.MediaType
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import mk.webfactory.template.network.api.MovieApiService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

/**
 * Tests for [MovieRepository]
 */
@RunWith(MockitoJUnitRunner::class)
class MovieRemoteDataSourceTest {

    private val mockMovie = mock(Movie::class.java)
    private val movieItemsAtPage1 = listOf<Show>(mockMovie)
    private val moviePage1Response = PaginatedResponse<Show>(1, 10, movieItemsAtPage1)

    private val mockTvShow = mock(TvShow::class.java)
    private val tvShowItemsAtPage1 = listOf<Show>(mockTvShow)
    private val tvShowPage1Response = PaginatedResponse<Show>(1, 10, tvShowItemsAtPage1)

    @Mock
    lateinit var movieApiService: MovieApiService
    lateinit var movieRemoteDataSource: MovieRemoteDataSource

    @Before
    fun setUp() {
        movieRemoteDataSource = MovieRemoteDataSource("userId", movieApiService)

        `when`(movieApiService.getTrending(eq(MediaType.MOVIE), any(), eq(1)))
            .thenReturn(Single.just(moviePage1Response))
        `when`(movieApiService.getTrending(eq(MediaType.TV), any(), eq(1)))
            .thenReturn(Single.just(tvShowPage1Response))
    }

    @Test
    fun getTrendingGroupedResponse() {
        val retrievedItems = movieRemoteDataSource.getTrendingShows(1).blockingGet()

        assertEquals(listOf(mockMovie, mockTvShow), retrievedItems.items)
    }
}