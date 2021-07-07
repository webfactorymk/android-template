package mk.webfactory.template.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import mk.webfactory.template.di.scope.user.UserScopeComponentManager
import mk.webfactory.template.feature.home.ui.adapter.ShowPagingSource
import mk.webfactory.template.model.movie.Show
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userScopeComponentManager: UserScopeComponentManager
) : ViewModel() {

    private val movieRepository = userScopeComponentManager.entryPoint.movieRepository()

    private val defaultPagingConfig = PagingConfig(pageSize = 50, enablePlaceholders = false)

    val popularMovies: Flow<PagingData<Show>> by lazy {
        Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                ShowPagingSource<Show> { page ->
                    movieRepository.getPopularMovies(page).blockingGet()
                }
            }
        ).flow
    }

    val popularTvShows: Flow<PagingData<Show>> by lazy {
        Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                ShowPagingSource<Show> { page ->
                    movieRepository.getPopularTvShows(page).blockingGet()
                }
            }
        ).flow
    }

    val trendingShows: Flow<PagingData<Show>> by lazy {
        Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                ShowPagingSource<Show> { page ->
                    movieRepository.getTrendingShows(page).blockingGet()
                }
            }
        ).flow
    }
}

