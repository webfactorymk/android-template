package mk.webfactory.template.user

import androidx.annotation.CheckResult
import dagger.Lazy
import io.reactivex.Completable
import io.reactivex.Completable.mergeDelayError
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.storage.Storage
import mk.webfactory.storage.StorageCache
import mk.webfactory.template.di.qualifier.Internal
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager<U> @Inject constructor(
    userStorage: Storage<U>,
    @Internal
    private val updateStream: BehaviorSubject<U>,
    private val loginHooks: Lazy<Set<@JvmSuppressWildcards LoginHook<U>>>,
    private val logoutHooks: Lazy<Set<@JvmSuppressWildcards LogoutHook>>
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
            .flatMap { user ->
                mergeDelayError(loginHooks.get().map { it.postLogin(user) })
                    .doOnError { Timber.e(it) }
                    .onErrorComplete()
                    .andThen(Single.just(user))
            }
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
            .andThen(mergeDelayError(logoutHooks.get().map { it.postLogout() }))
            .doOnError { Timber.e(it) }
            .onErrorComplete()
    }

    internal companion object {
        fun <U> hideUpdatesStream(updateStream: BehaviorSubject<U>): Observable<U> {
            return updateStream.hide().distinctUntilChanged()
        }
    }
}