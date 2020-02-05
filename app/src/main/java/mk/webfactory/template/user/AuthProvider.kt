package mk.webfactory.template.user

import io.reactivex.Single
import mk.webfactory.template.model.auth.AccessToken
/**
 * Provides login and logout mechanism for the user [User].
 */
interface AuthProvider<User> {
    fun login(): Single<User>
    fun logout(token: AccessToken)
}