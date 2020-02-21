package mk.webfactory.template.user

import io.reactivex.Single

/**
 * Provides login and logout mechanism for a [User].
 */
interface AuthProvider<User> {

    /**
     * Logs in the user and emits the logged in user value.
     */
    fun login(): Single<User>

    /**
     * Logs out the user and always emits a logged out representation of the user.
     */
    fun logout(user: User): Single<User>
}