package mk.webfactory.template.user

import androidx.annotation.CheckResult
import dagger.Lazy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Completable.mergeDelayError
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import mk.webfactory.storage.Storage
import mk.webfactory.template.di.qualifier.Internal
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.network.UnauthorizedUserException
import mk.webfactory.template.network.api.UserApiService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager<U> @Inject constructor(
    private val userStore: Storage<U>,
    private val userApiService: UserApiService,
    @Internal private val updateStream: BehaviorSubject<U>,
    private val userEventHooks: Lazy<Set<@JvmSuppressWildcards UserEventHook<U>>>
) {

    /**
     * Called to ensure the user is loaded from disk and awaits all
     * [UserEventHook.onUserLoaded] hooks.
     */
    @CheckResult
    fun preloadUser(): Completable = userStore.get()
        .doAfterSuccess(updateStream::onNext)
        .flatMapCompletable { user ->
            mergeDelayError(
                userEventHooks.get().map { it.onUserLoaded(user) })
        }
        .doOnError { Timber.e(it) }
        .onErrorComplete()

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
    //todo modify behavior to fit your need, this is a very lazy example
    fun isLoggedIn() = userStore.get().blockingGet()?.let {
        if (it is UserSession) it.isActive else true
    } ?: false

    /**
     *  Logs-in the user using the provided [AuthDelegate]
     *  and triggers an update to the user [updateStream].
     *
     *  If the user is already logged in, the user is returned with no additional action.
     */
    @CheckResult
    fun login(authDelegate: AuthDelegate<U>): Single<U> = login(authDelegate::login)

    @CheckResult
    fun login(loginDelegateFn: () -> Single<U>): Single<U> {
        if (isLoggedIn()) {
            return userStore.get().toSingle()
        }
        return loginDelegateFn()
            .flatMap { user -> userStore.save(user).doOnError { Timber.e(it) } }
            .flatMap { user ->
                mergeDelayError(userEventHooks.get().map { it.postLogin(user) })
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
    @CheckResult
    fun logout(authDelegate: AuthDelegate<U>): Completable = logout(authDelegate::logout)

    @CheckResult
    fun logout(logoutDelegateFn: (user: U) -> Single<U>): Completable {
        if (!isLoggedIn()) {
            return Completable.complete()
        }
        return logoutDelegateFn(userStore.get().blockingGet()!!)
            .doAfterSuccess(updateStream::onNext)
            .ignoreElement()
            .andThen(userStore.delete())
            .andThen(mergeDelayError(userEventHooks.get().map { it.postLogout() }))
            .doOnError { Timber.e(it) }
            .onErrorComplete()
    }

    @CheckResult
    fun updateUser(newUser: U): Single<U> = Single.fromCallable {
        if (isLoggedIn().not()) {
            Timber.e("UserManager - Updating user error: User logged out!")
            userStore.delete().blockingAwait()
            throw UnauthorizedUserException("Updating user on logged out usr")
        }
        newUser
    }
        //.flatMap { userApiService.updateUserProfile(it) }
        .flatMap { userStore.save(it) }
        .doAfterSuccess(updateStream::onNext)

    internal companion object {
        fun <U> hideUpdatesStream(updateStream: BehaviorSubject<U>): Observable<U> {
            return updateStream.hide().distinctUntilChanged()
        }
    }
}