package mk.webfactory.template.user

import io.reactivex.Observable
import mk.webfactory.template.data.storage.Storage

class UserStore<U>(_inner: Storage<U>) {

    private var inner: Storage<U> = _inner
    private var cachedUser: U? = null
    private var isUserFetched = false

    fun clearCache() {
        isUserFetched = false
    }

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

    fun save(user: U): Observable<U>? {
        return inner.save(user)?.doOnNext { t ->
            cachedUser = t
            isUserFetched = true
        }
    }

    fun delete() {
        inner.delete()
    }
}
