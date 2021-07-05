package mk.webfactory.template.feature.home

import androidx.annotation.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import mk.webfactory.template.di.qualifier.Local
import mk.webfactory.template.di.qualifier.Remote
import mk.webfactory.template.di.scope.user.UserScope
import timber.log.Timber
import javax.inject.Inject

/**
 * Concrete implementation to load data from multiple sources
 *
 *
 * This implements a synchronisation between cached data and data obtained from server,
 * by using the remote data source only if the cache doesn't exist, is empty or expired.
 */
@UserScope
class HomeRepository @Inject constructor(
    @NonNull @param:Remote private val homeRemoteDataSource: HomeDataSource,
    @NonNull @param:Local private val homeLocalDataSource: HomeDataSource
) : HomeDataSource {

    init {
        Timber.d("HomeRepository: Repository created")
    }

    override fun getSomething(): Maybe<Any> {
        if (homeLocalDataSource.isDataAvailable) {
            Timber.d("HomeRepository: Local data fetched")
            return homeLocalDataSource.getSomething()
        }
        if (homeRemoteDataSource.isDataAvailable) {
            Timber.d("HomeRepository: Remote data fetched")
            return homeRemoteDataSource.getSomething()
                .doOnSuccess { item -> homeLocalDataSource.setSomething(item)}
        }
        return Maybe.error(Exception("No data available"))
    }

    override fun setSomething(any: Any) {
        homeRemoteDataSource.setSomething(any)
        homeLocalDataSource.setSomething(any)
    }

    override val isDataAvailable: Boolean
        get() = homeRemoteDataSource.isDataAvailable || homeLocalDataSource.isDataAvailable

    override fun deleteData(): Completable {
        return deleteLocalData()
    }

    fun deleteLocalData(): Completable {
        Timber.d("HomeRepository: Deleting local data")
        return homeLocalDataSource.deleteData()
    }

}