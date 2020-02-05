package mk.webfactory.template.user

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.template.data.storage.Storage
import mk.webfactory.template.model.auth.AccessToken

open class UserManager<U>(
    private val updateStream: BehaviorSubject<U>,
    userStorage: Storage<U>
) {
    private val userStore: UserStore<U> = UserStore(userStorage)
    private var authProvider: AuthProvider<U>? = null

    /**
     * Receive updates on the current user, if any, and all subsequent users.
     *
     * @return [Observable] that emits the current user data, if any, and all subsequent user updates.
     */
    fun updates(): Observable<U> {
        return updateStream.hide().distinctUntilChanged()
    }

    fun getLoggedInUserBlocking() = userStore.get().blockingFirst()

    fun getLoggedInUser() = userStore.get()

    fun isLoggedIn() = authProvider != null && userStore.get().blockingFirst() != null

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
}