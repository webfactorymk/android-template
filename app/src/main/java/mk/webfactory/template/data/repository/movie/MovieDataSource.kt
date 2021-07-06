package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow

interface MovieDataSource {
    val userId: String

    fun getPopularMovies(page: Int): Single<PaginatedResponse<Movie>>
    fun setPopularMovies(
        page: Int,
        items: PaginatedResponse<Movie>
    ): Single<PaginatedResponse<Movie>> = Single.just(items)

    fun getPopularTvShows(page: Int): Single<PaginatedResponse<TvShow>>
    fun setPopularTvShows(
        page: Int,
        items: PaginatedResponse<TvShow>
    ): Single<PaginatedResponse<TvShow>> = Single.just(items)

    fun getTrendingShows(page: Int): Single<PaginatedResponse<Show>>
    fun setTrendingShows(
        page: Int,
        items: PaginatedResponse<Show>
    ): Single<PaginatedResponse<Show>> = Single.just(items)

    fun deleteCachedData(): Completable = Completable.complete()
}