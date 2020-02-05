package mk.webfactory.template.user

import io.reactivex.Observable
import io.reactivex.Single
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

    /**
     * Gets the logged in user.
     * @return [User]
     */
    fun getLoggedInUserBlocking() = userStore.get().blockingGet()

    /**
     * Subscribes for updates on logged in user
     * @return [Observable] that emits [User]
     */

    fun getLoggedInUser() = userStore.get()

    /**
     *  Checks if the user is logged in.
     *
     * <i>Note: Extensions can expand behavior,
     * but mind that this method is used in this component internally
     * and must be the single source of truth for the user's logged in status.</i>
     */

    fun isLoggedIn() = authProvider != null && userStore.get().blockingGet() != null

    /**
     *  Logs-in the user using the provided [AuthProvider]
     *   and triggers an update to the user [updateStream].
     *
     * If the user is already logged in, the user is returned with no additional action.
     */
    fun login(authProvider: AuthProvider<U>): Single<U> {
        this.authProvider = authProvider
        if (isLoggedIn()) {
            return userStore.get()
        }
        return authProvider.login().flatMap { t -> userStore.save(t) }
            .doAfterSuccess(updateStream::onNext)
    }

    /**
     * Logs-out the user
     * If the user is already logged out, the method completes without doing anything.
     */
    fun logout(credentials: AccessToken) {
        if (!(isLoggedIn())) {
            return
        }
        authProvider?.logout(credentials)
        userStore.delete()
    }
}