package mk.webfactory.template.feature.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.di.scope.user.UserScopeComponentManager
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userScopeComponentManager: UserScopeComponentManager
) : ViewModel() {

    private val movieRepository = userScopeComponentManager.entryPoint.movieRepository()

    private var moviesDisposable: Disposable? = null
    private var tvShowsDisposable: Disposable? = null
    private var trendingDisposable: Disposable? = null

    private val _moviesProgressBarState = MutableLiveData(true)
    val moviesProgressBarState: LiveData<Boolean> = _moviesProgressBarState

    private val _tvShowsProgressBarState = MutableLiveData(true)
    val tvShowsProgressBarState: LiveData<Boolean> = _tvShowsProgressBarState

    private val _trendingProgressBarState = MutableLiveData<Boolean>(true)
    val trendingProgressBarState: LiveData<Boolean> = _trendingProgressBarState

    private var stateError = MutableLiveData<Throwable?>(null)
    fun getStateError(): LiveData<Throwable?> = stateError

    fun getPopularMovies(): LiveData<List<Movie>> = popularMovies
    fun getPopularTvShows(): LiveData<List<TvShow>> = popularTvShows
    fun getTrending(): LiveData<List<Show>> = trending

    private val popularMovies: MutableLiveData<List<Movie>> by lazy {
        MutableLiveData<List<Movie>>().also {
            loadPopularMovies()
        }
    }

    private val popularTvShows: MutableLiveData<List<TvShow>> by lazy {
        MutableLiveData<List<TvShow>>().also {
            loadPopularTvShows()
        }
    }

    private val trending: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>().also {
            loadTrending()
        }
    }

    private fun loadPopularMovies() {
        moviesDisposable.safeDispose()
        _moviesProgressBarState.value = true
        moviesDisposable = movieRepository.getPopularMovies(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _moviesProgressBarState.value = false
                    stateError.value = null
                    popularMovies.value = it
                },
                onError = {
                    _moviesProgressBarState.value = false
                    stateError.value = it
                    Timber.e(it)
                }
            )
    }

    private fun loadPopularTvShows() {
        tvShowsDisposable.safeDispose()
        _tvShowsProgressBarState.value = true
        tvShowsDisposable = movieRepository.getPopularTvShows(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _tvShowsProgressBarState.value = false
                    stateError.value = null
                    popularTvShows.value = it
                },
                onError = {
                    _tvShowsProgressBarState.value = false
                    stateError.value = it
                    Timber.e(it)
                }
            )
    }

    private fun loadTrending() {
        trendingDisposable.safeDispose()
        _trendingProgressBarState.value = true
        trendingDisposable = movieRepository.getTrendingShows(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _trendingProgressBarState.value = false
                    stateError.value = null
                    trending.value = it
                },
                onError = {
                    _trendingProgressBarState.value = false
                    stateError.value = it
                    Timber.e(it)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        moviesDisposable.safeDispose()
        tvShowsDisposable.safeDispose()
        trendingDisposable.safeDispose()
    }
}

