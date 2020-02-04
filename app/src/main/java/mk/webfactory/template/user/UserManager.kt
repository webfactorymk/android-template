package mk.webfactory.template.user

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.template.data.storage.Storage
import mk.webfactory.template.di.qualifier.Internal
import mk.webfactory.template.model.auth.AccessToken
import javax.inject.Inject
import javax.inject.Named

class UserManager<U>(
    private val updateStream: BehaviorSubject<U>,
    userStorage: Storage<U>
) {
    private val userStore: UserStore<U> = UserStore(userStorage)
    private var authProvider: AuthProvider<U>? = null

    fun getLoggedInUser(): Observable<U>? {
        return userStore.get()
    }

    fun isLoggedIn(): Boolean {
        return userStore.get()?.blockingFirst() != null && authProvider != null
    }

    fun login(authProvider: AuthProvider<U>, credentials: AccessToken): Observable<U>? {
        this.authProvider = authProvider
        if (isLoggedIn()) {
            return userStore.get()
        }
        return authProvider.login(credentials)
            .doAfterSuccess { t -> userStore.save(t) }.toObservable()
    }

    fun logout(credentials: AccessToken) {
        if (!(isLoggedIn())) {
            return
        }
        authProvider?.logout(credentials)
        userStore.delete()
    }

    fun teardown() {
        userStore.clearCache()
        authProvider = null
    }
}