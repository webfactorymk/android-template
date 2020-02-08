package mk.webfactory.template.user

import io.reactivex.Completable

/**
 * Provides actions to be executed during the [UserManager&#39;s][UserManager]
 * logout process.
 *
 * The UserManager provided by an object graph will by default gather all LogoutHooks
 * provided along [@IntoSet] by modules from the same object graph.
 *
 * Example:
 * ```
 *     @Provides @IntoSet LogoutHook provideCleanUserDirLogoutHook() { ... }
 * ```
 */
interface LogoutHook {

    fun postLogout(): Completable
}