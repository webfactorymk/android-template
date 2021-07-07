package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.api.empty
import mk.webfactory.template.model.movie.MediaType.MOVIE
import mk.webfactory.template.model.movie.MediaType.TV
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TimeWindow
import mk.webfactory.template.model.movie.TvShow
import mk.webfactory.template.network.api.MovieApiService

/**
 * Concrete implementation of a data source that draws data from server.
 */
class MovieRemoteDataSource(
    override val userId: String,
    private val movieApiService: MovieApiService,
) : MovieDataSource {

    override fun getPopularMovies(page: Int): Single<PaginatedResponse<Movie>> =
        movieApiService.getPopularMovies(page)

    override fun getPopularTvShows(page: Int): Single<PaginatedResponse<TvShow>> =
        movieApiService.getPopularTvShows(page)

    override fun getTrendingShows(page: Int): Single<PaginatedResponse<Show>> = Single.zip(
        movieApiService.getTrending(MOVIE, TimeWindow.DAY, page).onErrorReturn { empty() },
        movieApiService.getTrending(TV, TimeWindow.DAY, page).onErrorReturn { empty() },
        { movies, tvShows ->
            PaginatedResponse(
                page = page,
                totalPages = maxOf(movies.totalPages, tvShows.totalPages),
                items = movies.items + tvShows.items
            )
        }
    )
}