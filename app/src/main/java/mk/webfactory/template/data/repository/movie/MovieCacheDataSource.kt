package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.data.DataNotFoundException
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import java.lang.IndexOutOfBoundsException
import java.util.*

/**
 * Simple in-memory cache implementation of a data source.
 */
class MovieCacheDataSource(override val userId: String) : MovieDataSource {
    private val moviesCache = Collections.synchronizedList(mutableListOf<List<Movie>>())
    private val tvShowsCache = Collections.synchronizedList(mutableListOf<List<TvShow>>())
    private val trendingCache = Collections.synchronizedList(mutableListOf<List<Show>>())

    override fun getPopularMovies(page: Int): Single<List<Movie>> = Single.fromCallable {
        moviesCache.elementAt(index = page.minus(1).coerceAtLeast(0))
    }.onErrorResumeWith(Single.error(DataNotFoundException()))

    override fun setPopularMovies(page: Int, items: List<Movie>): Single<List<Movie>> =
        Completable.fromAction { saveItems(page, items, moviesCache) }
            .andThen(Single.just(items)).onErrorReturn { items }

    override fun getPopularTvShows(page: Int): Single<List<TvShow>> = Single.fromCallable {
        tvShowsCache.elementAt(index = page.minus(1).coerceAtLeast(0))
    }.onErrorResumeWith(Single.error(DataNotFoundException()))

    override fun setPopularTvShows(page: Int, items: List<TvShow>): Single<List<TvShow>> =
        Completable.fromAction { saveItems(page, items, tvShowsCache) }
            .andThen(Single.just(items)).onErrorReturn { items }

    override fun getTrendingShows(page: Int): Single<List<Show>> = Single.fromCallable {
        trendingCache.elementAt(index = page.minus(1).coerceAtLeast(0))
    }.onErrorResumeWith(Single.error(DataNotFoundException()))

    override fun setTrendingShows(page: Int, items: List<Show>): Single<List<Show>> =
        Completable.fromAction { saveItems(page, items, trendingCache) }
            .andThen(Single.just(items)).onErrorReturn { items }

    override fun deleteCachedData(): Completable = Completable.fromAction {
        moviesCache.clear()
        tvShowsCache.clear()
        trendingCache.clear()
    }

    private fun <T> saveItems(page: Int, items: List<T>, cache: MutableList<List<T>>) {
        val pageIndex = page.minus(1).coerceAtLeast(0)
        if (pageIndex > cache.size || page < 0) {
            throw IndexOutOfBoundsException("Page $page out of order (${cache.size}), don't cache")
        }
        for (index in cache.size - 1 downTo pageIndex) {
            // if someone saves page 2 out of cached 4,
            // then page 2 is overwritten and page 3 and 4 are considered obsolete and deleted
            cache.removeLast()
        }
        cache.add(items)
    }
}