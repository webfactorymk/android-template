package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow

interface MovieDataSource {
    val userId: String

    fun getPopularMovies(page: Int): Single<List<Movie>>
    fun setPopularMovies(page: Int, items: List<Movie>): Single<List<Movie>> = Single.just(items)

    fun getPopularTvShows(page: Int): Single<List<TvShow>>
    fun setPopularTvShows(page: Int, items: List<TvShow>): Single<List<TvShow>> = Single.just(items)

    fun getTrendingShows(page: Int): Single<List<Show>>
    fun setTrendingShows(page: Int, items: List<Show>): Single<List<Show>> = Single.just(items)

    fun deleteCachedData(): Completable = Completable.complete()
}