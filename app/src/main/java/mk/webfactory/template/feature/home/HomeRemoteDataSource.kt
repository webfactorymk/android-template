package mk.webfactory.template.feature.home

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import mk.webfactory.template.network.api.UserService

/**
 * Concrete implementation of a data source that draws data from server.
 */
class HomeRemoteDataSource(userId: String, private val userService: UserService) :
    HomeDataSource {

    override fun getSomething(): Maybe<Any> {
        throw UnsupportedOperationException("Not implemented yet!")
        //return userService.getSomething();
    }

    override fun setSomething(any: Any) {
        //no-op
    }

    override fun deleteData(): Completable {
        return Completable.complete()
    }

    override val isDataAvailable: Boolean
        get() = true
}