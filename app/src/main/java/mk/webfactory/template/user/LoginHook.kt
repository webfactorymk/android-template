package mk.webfactory.template.user

import io.reactivex.rxjava3.core.Completable

/**
 * Provides actions to be executed during the [UserManager&#39;s][UserManager]
 * login process.
 *
 * The UserManager provided by an object graph will by default gather all LoginHooks
 * provided along [@IntoSet] by modules from the same object graph.
 *
 * Example:
 * ```
 *     @Provides @IntoSet LoginHook provideUserScopeCreatorLoginHook() { ... }
 * ```
 */
interface LoginHook<U> {

    fun postLogin(user: U): Completable
}