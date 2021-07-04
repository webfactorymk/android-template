package mk.webfactory.template.user

import io.reactivex.rxjava3.core.Single

/**
 * Delegates the login and logout mechanism for a [User].
 */
interface AuthDelegate<User> {

    /**
     * Logs in the user and emits the logged in user value.
     */
    fun login(): Single<User>

    /**
     * Logs out the user and always emits a logged out representation of the user.
     */
    fun logout(user: User): Single<User>
}