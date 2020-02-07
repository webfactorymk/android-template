package mk.webfactory.template.user

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.storage.Storage
import mk.webfactory.storage.StorageCache
import timber.log.Timber

open class UserManager<U>(
    private val updateStream: BehaviorSubject<U>,
    userStorage: Storage<U>
) {
    private val userStore: StorageCache<U> = StorageCache(userStorage)
    private var authProvider: AuthProvider<U>? = null

    /**
     * Receive distinct updates on the current user, if any, and all subsequent users.
     */
    @CheckResult
    fun updates(): Observable<U> {
        return hideUpdatesStream(updateStream)
    }

    /**
     * Gets the logged in user or null if no user is logged in.
     */
    fun getLoggedInUserBlocking(): U? = userStore.get().blockingGet()

    /**
     * Gets the logged in user or completes if no user is logged in.
     */
    @CheckResult
    fun getLoggedInUser(): Maybe<U> = userStore.get()

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
     *  and triggers an update to the user [updateStream].
     *
     * If the user is already logged in, the user is returned with no additional action.
     */
    fun login(authProvider: AuthProvider<U>): Single<U> {
        this.authProvider = authProvider
        if (isLoggedIn()) {
            return userStore.get().toSingle()
        }
        return authProvider.login()
            .flatMap { user -> userStore.save(user).doOnError { Timber.e(it) } }
            .doAfterSuccess(updateStream::onNext)
    }

    /**
     * Logs-out the user.
     * If the user is already logged out, the method completes without doing anything.
     */
    fun logout(): Completable {
        if (!isLoggedIn()) {
            return Completable.complete()
        }
        return userStore.delete()
            .andThen(authProvider!!.logout())
            .doOnError { Timber.e(it) }
            .onErrorComplete()
    }

    companion object {
        fun <U> hideUpdatesStream(updateStream: BehaviorSubject<U>): Observable<U> {
            return updateStream.hide().distinctUntilChanged()
        }
    }
}