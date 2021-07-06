package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.data.DataNotFoundException
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import java.lang.IndexOutOfBoundsException
import java.util.*

/**
 * Simple in-memory cache implementation of a data source.
 */
class MovieCacheDataSource(override val userId: String) : MovieDataSource {
    private val moviesCache =
        Collections.synchronizedList(mutableListOf<PaginatedResponse<Movie>>())
    private val tvShowsCache =
        Collections.synchronizedList(mutableListOf<PaginatedResponse<TvShow>>())
    private val trendingCache =
        Collections.synchronizedList(mutableListOf<PaginatedResponse<Show>>())

    override fun getPopularMovies(page: Int): Single<PaginatedResponse<Movie>> =
        Single.fromCallable {
            moviesCache.elementAt(index = page.minus(1).coerceAtLeast(0))
        }.onErrorResumeWith(Single.error(DataNotFoundException()))

    override fun setPopularMovies(
        page: Int,
        items: PaginatedResponse<Movie>
    ): Single<PaginatedResponse<Movie>> =
        Completable.fromAction { saveItems(page, items, moviesCache) }
            .andThen(Single.just(items))
            .onErrorReturn { items }

    override fun getPopularTvShows(page: Int): Single<PaginatedResponse<TvShow>> =
        Single.fromCallable { tvShowsCache.elementAt(index = page.minus(1).coerceAtLeast(0)) }
            .onErrorResumeWith(Single.error(DataNotFoundException()))

    override fun setPopularTvShows(
        page: Int,
        items: PaginatedResponse<TvShow>
    ): Single<PaginatedResponse<TvShow>> =
        Completable.fromAction { saveItems(page, items, tvShowsCache) }
            .andThen(Single.just(items))
            .onErrorReturn { items }

    override fun getTrendingShows(page: Int): Single<PaginatedResponse<Show>> =
        Single.fromCallable { trendingCache.elementAt(index = page.minus(1).coerceAtLeast(0)) }
            .onErrorResumeWith(Single.error(DataNotFoundException()))

    override fun setTrendingShows(
        page: Int,
        items: PaginatedResponse<Show>
    ): Single<PaginatedResponse<Show>> =
        Completable.fromAction { saveItems(page, items, trendingCache) }
            .andThen(Single.just(items))
            .onErrorReturn { items }

    override fun deleteCachedData(): Completable = Completable.fromAction {
        moviesCache.clear()
        tvShowsCache.clear()
        trendingCache.clear()
    }

    private fun <Page> saveItems(page: Int, items: Page, cache: MutableList<Page>) {
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