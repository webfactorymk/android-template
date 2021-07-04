package mk.webfactory.template.user

import io.reactivex.rxjava3.core.Completable

/**
 * User Event Hooks
 *
 * These hooks provide a mechanism for components to be configured
 * to perform actions automatically when certain user events happen.
 * For example cleanup user files on logout, enable/disable push
 * notifications, etc.
 *
 * Implement [UserEventHook], override needed methods
 * and register your implementation when creating [UserManager].
 */
interface UserEventHook<U> {

    /**
     * Provides actions to be executed during the [UserManager&#39;s][UserManager]
     * login process.
     *
     * Note that this action happens only when the user explicitly logs in,
     * not when the app is restarted and the logged in user is loaded.
     * For every user update event see [UserManager.updates] or [onUserLoaded].
     *
     * The UserManager provided by an object graph will by default gather all LoginHooks
     * provided along [@IntoSet] by modules from the same object graph.
     *
     * Example:
     * ```
     *     @Provides @IntoSet LoginHook provideUserScopeCreatorLoginHook() { ... }
     * ```
     */
    fun postLogin(user: U): Completable = Completable.complete()

    /**
     * Provides actions to be executed during the [UserManager&#39;s][UserManager]
     * logout process.
     *
     * Note that this action happens only when the user explicitly logs out,
     * not when the session expires or the app is restarted without logged in user.
     * For every user update event see [UserManager.updates] or [onUserLoaded].
     *
     * The UserManager provided by an object graph will by default gather all LogoutHooks
     * provided along [@IntoSet] by modules from the same object graph.
     *
     * Example:
     * ```
     *     @Provides @IntoSet LogoutHook provideCleanUserDirLogoutHook() { ... }
     * ```
     */
    fun postLogout(): Completable = Completable.complete()

    /**
     * Called when the user is first loaded at app start.
     * Might be `null` if the user is not logged in.
     *
     * <i>This action is awaited in the [UserManager]'s init method so
     * you can put any initialization that needs happen before the app
     * UI is loaded (for example setup user scoped components).</i>
     */
    fun onUserLoaded(user: U?): Completable = Completable.complete()
}