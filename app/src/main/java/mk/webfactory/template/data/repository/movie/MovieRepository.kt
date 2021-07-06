package mk.webfactory.template.data.repository.movie

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.di.qualifier.Local
import mk.webfactory.template.di.qualifier.Remote
import mk.webfactory.template.di.scope.user.UserScope
import mk.webfactory.template.model.api.PaginatedResponse
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

/**
 * Concrete implementation of [MovieDataSource] that loads data from multiple sources
 *
 * This implements a synchronization between cached data and data obtained from server,
 * by using the remote data source only if the cache doesn't exist, is empty or expired.
 */
@UserScope
class MovieRepository @Inject constructor(
    @param:Named("userId") override val userId: String,
    @param:Remote private val movieRemoteDataSource: MovieDataSource,
    @param:Local private val movieCacheDataSource: MovieDataSource,
) : MovieDataSource {

    init {
        require(
            userId == movieRemoteDataSource.userId && userId == movieCacheDataSource.userId
        ) { "Can't have data sources from different users" }
        Timber.d("MovieRepository: Repository created")
    }

    override fun getPopularMovies(page: Int): Single<PaginatedResponse<Movie>> =
        movieCacheDataSource.getPopularMovies(page)
            .onErrorResumeNext {
                movieRemoteDataSource.getPopularMovies(page)
                    .flatMap { items -> movieCacheDataSource.setPopularMovies(page, items) }
            }

    override fun getPopularTvShows(page: Int): Single<PaginatedResponse<TvShow>> =
        movieCacheDataSource.getPopularTvShows(page)
            .onErrorResumeNext {
                movieRemoteDataSource.getPopularTvShows(page)
                    .flatMap { items -> movieCacheDataSource.setPopularTvShows(page, items) }
            }

    override fun getTrendingShows(page: Int): Single<PaginatedResponse<Show>> =
        movieCacheDataSource.getTrendingShows(page)
            .onErrorResumeNext {
                movieRemoteDataSource.getTrendingShows(page)
                    .flatMap { items -> movieCacheDataSource.setTrendingShows(page, items) }
            }

    override fun deleteCachedData(): Completable {
        Timber.d("MovieRepository: Deleting local data")
        return movieCacheDataSource.deleteCachedData()
    }
}