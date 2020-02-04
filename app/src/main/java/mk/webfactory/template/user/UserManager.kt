package mk.webfactory.template.user

import io.reactivex.Observable
import mk.webfactory.template.data.storage.Storage
import mk.webfactory.template.model.auth.AccessToken


class UserManager<U>(userStorage: Storage<U>, auth: AuthProvider<U>) {
    var userStore: UserStore<U> = UserStore(userStorage)
    var authProvider: AuthProvider<U>? = auth


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