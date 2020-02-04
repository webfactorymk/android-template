package mk.webfactory.template.user

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Observable
import mk.webfactory.template.data.storage.Storage

class UserStore<U>(private val inner: Storage<U>) {

    private var cachedUser: U? = null
    private var isUserFetched = false

    fun clearCache() {
        isUserFetched = false
    }

    @CheckResult
    fun get(): Observable<U>? {
        return if (isUserFetched) {
            Observable.just(cachedUser)
        } else {
            inner.retrieve()?.doOnNext { t ->
                cachedUser = t
                isUserFetched = true
            }
        }
    }

    @CheckResult
    fun save(user: U): Observable<U> {
        return inner.save(user)?.doOnNext { t ->
            cachedUser = t
            isUserFetched = true
        }
    }

    @CheckResult
    fun delete(): Completable {
        inner.delete()
    }
}
