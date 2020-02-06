package mk.webfactory.template.user

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import mk.webfactory.template.data.storage.Storage

/**
 * Storage implementation with cache.
 */
class UserStore<U>(private val inner: Storage<U>) {



    //FIXME refactor this with new storage interface and its usage in user manager
    //FIXME make threadsafe, and document
    //FIXME make generic and put in storage package



    private var cachedUser: U? = null
    private var isUserFetched = false

    /**
     * Gets the user, caching the value.
     */
    @CheckResult
    fun get(): Maybe<U> {
        return if (isUserFetched) {
            Single.just(cachedUser)
        } else {
            inner.retrieve().doOnSuccess { t ->
                cachedUser = t
                isUserFetched = true
            }
        }
    }

    @CheckResult
    fun save(user: U): Single<U> {
        return inner.save(user).doOnSuccess { t ->
            cachedUser = t
            isUserFetched = true
        }
    }

    @CheckResult
    fun delete(): Completable {
        cachedUser = null
        return inner.delete()
    }

    fun clearCache() {
        isUserFetched = false
    }
}
