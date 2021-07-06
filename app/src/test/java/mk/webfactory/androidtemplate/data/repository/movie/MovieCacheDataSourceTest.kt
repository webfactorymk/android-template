package mk.webfactory.androidtemplate.data.repository.movie

import mk.webfactory.template.data.DataNotFoundException
import mk.webfactory.template.data.repository.movie.MovieCacheDataSource
import mk.webfactory.template.model.movie.Movie
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Tests for [MovieCacheDataSource]
 */
@RunWith(MockitoJUnitRunner::class)
class MovieCacheDataSourceTest {

    private val mockMovie1 = mock(Movie::class.java)
    private val mockMovie2 = mock(Movie::class.java)
    private val mockMovie3 = mock(Movie::class.java)

    private val itemsAtPage1 = listOf<Movie>(mockMovie1)
    private val itemsAtPage2 = listOf<Movie>(mockMovie2)
    private val itemsAtPage3 = listOf<Movie>(mockMovie3)

    lateinit var dataSource: MovieCacheDataSource

    @Before
    fun setUp() {
        dataSource = MovieCacheDataSource("userId")
    }

    @Test
    fun testSaveAndRetrieve() {
        val saveOpResult = dataSource.setPopularMovies(1, itemsAtPage1).blockingGet()
        val retrievedItems = dataSource.getPopularMovies(1).blockingGet()

        assertEquals(itemsAtPage1, saveOpResult)
        assertEquals(itemsAtPage1, retrievedItems)
    }

    @Test
    fun testPage0And1AreTheSame() {
        val saveOpResult = dataSource.setPopularMovies(0, itemsAtPage1).blockingGet()
        val retrievedItems = dataSource.getPopularMovies(1).blockingGet()

        assertEquals(itemsAtPage1, saveOpResult)
        assertEquals(itemsAtPage1, retrievedItems)
    }

    @Test
    fun testPage1And0AreTheSame() {
        val saveOpResult = dataSource.setPopularMovies(0, itemsAtPage1).blockingGet()
        val retrievedItems = dataSource.getPopularMovies(1).blockingGet()

        assertEquals(itemsAtPage1, saveOpResult)
        assertEquals(itemsAtPage1, retrievedItems)
    }

    @Test
    fun testRetrieveEmpty() {
        val emptyList: List<Movie> = emptyList()
        val saveOpResult = dataSource.setPopularMovies(1, emptyList()).blockingGet()
        val retrievedItems = dataSource.getPopularMovies(1).blockingGet()

        assertEquals(emptyList, saveOpResult)
        assertEquals(emptyList, retrievedItems)
    }

    @Test(expected = DataNotFoundException::class)
    fun testRetrieveNonExisting() {
        dataSource.getPopularMovies(5).blockingGet()
    }

    @Test
    fun testSaveNegativePage() {
        val saveOpResult = dataSource.setPopularMovies(-1, itemsAtPage3).blockingGet()

        assertEquals(itemsAtPage3, saveOpResult)
        assertThrows(DataNotFoundException::class.java) {
            dataSource.getPopularMovies(1).blockingGet()
        }
    }

    @Test
    fun testSaveButSkipPages() {
        val saveOpResult = dataSource.setPopularMovies(3, itemsAtPage3).blockingGet()

        assertEquals(itemsAtPage3, saveOpResult)
        assertThrows(DataNotFoundException::class.java) {
            dataSource.getPopularMovies(1).blockingGet()
        }
    }

    @Test
    fun testSaveMultiple() {
        val saveOpResult1 = dataSource.setPopularMovies(1, itemsAtPage1).blockingGet()
        val saveOpResult2 = dataSource.setPopularMovies(2, itemsAtPage2).blockingGet()
        val saveOpResult3 = dataSource.setPopularMovies(3, itemsAtPage3).blockingGet()

        val retrievedItems1 = dataSource.getPopularMovies(1).blockingGet()
        val retrievedItems2 = dataSource.getPopularMovies(2).blockingGet()
        val retrievedItems3 = dataSource.getPopularMovies(3).blockingGet()

        assertEquals(itemsAtPage1, saveOpResult1)
        assertEquals(itemsAtPage1, retrievedItems1)
        assertEquals(itemsAtPage2, saveOpResult2)
        assertEquals(itemsAtPage2, retrievedItems2)
        assertEquals(itemsAtPage3, saveOpResult3)
        assertEquals(itemsAtPage3, retrievedItems3)
    }

    @Test
    fun testOverwrite() {
        val saveOpResult1 = dataSource.setPopularMovies(1, itemsAtPage1).blockingGet()
        val saveOpResult2old = dataSource.setPopularMovies(2, itemsAtPage2).blockingGet()
        val saveOpResult2new = dataSource.setPopularMovies(2, itemsAtPage3).blockingGet()

        val retrievedItems = dataSource.getPopularMovies(2).blockingGet()

        assertEquals(itemsAtPage1, saveOpResult1)
        assertEquals(itemsAtPage2, saveOpResult2old)
        assertEquals(itemsAtPage3, saveOpResult2new)
        assertEquals(itemsAtPage3, retrievedItems)
    }

    @Test
    fun testOverwriteMultiple() {
        dataSource.setPopularMovies(1, itemsAtPage1).blockingGet()
        dataSource.setPopularMovies(2, itemsAtPage2).blockingGet()
        dataSource.setPopularMovies(3, itemsAtPage3).blockingGet()

        val saveOpResult = dataSource.setPopularMovies(2, itemsAtPage3).blockingGet()
        val retrievedItems = dataSource.getPopularMovies(2).blockingGet()

        assertEquals(itemsAtPage3, saveOpResult)
        assertEquals(itemsAtPage3, retrievedItems)
        assertThrows(DataNotFoundException::class.java) {
            dataSource.getPopularMovies(3).blockingGet()
        }
    }
}