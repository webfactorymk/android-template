package mk.webfactory.template.user

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import mk.webfactory.template.data.storage.Storage

/**
 * Storage implementation that caches the value and returns
 * null instead of error if the value is not stored.
 */
class UserStore<U>(private val inner: Storage<U>) {

    private var cachedUser: U? = null
    private var isUserFetched = false

    /**
     * Gets the user, caching the value.
     */
    @CheckResult
    fun get(): Single<U> {
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
