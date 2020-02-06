package mk.webfactory.template.user

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Provides login and logout mechanism for a [User].
 */
interface AuthProvider<User> {

    fun login(): Single<User>

    fun logout(): Completable
}