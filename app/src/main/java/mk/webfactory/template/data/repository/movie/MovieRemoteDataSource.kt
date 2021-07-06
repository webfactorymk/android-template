package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.api.paginatedItemsMapper
import mk.webfactory.template.model.movie.*
import mk.webfactory.template.model.movie.MediaType.MOVIE
import mk.webfactory.template.model.movie.MediaType.TV
import mk.webfactory.template.network.api.MovieApiService

/**
 * Concrete implementation of a data source that draws data from server.
 */
class MovieRemoteDataSource(
    override val userId: String,
    private val movieApiService: MovieApiService,
) : MovieDataSource {

    override fun getPopularMovies(page: Int): Single<List<Movie>> =
        movieApiService.getPopularMovies(page).map(paginatedItemsMapper())

    override fun getPopularTvShows(page: Int): Single<List<TvShow>> =
        movieApiService.getPopularTvShows(page).map(paginatedItemsMapper())

    override fun getTrendingShows(page: Int): Single<List<Show>> = Single.zip(
        movieApiService.getTrending(page, MOVIE, TimeWindow.DAY).map(paginatedItemsMapper()),
        movieApiService.getTrending(page, TV, TimeWindow.DAY).map(paginatedItemsMapper()),
        { movies, tvShows -> movies + tvShows }
    )
}