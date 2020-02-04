package mk.webfactory.template.user

import io.reactivex.Single
import mk.webfactory.template.model.auth.AccessToken

interface AuthProvider<User> {
    fun login(token: AccessToken): Single<User>
    fun logout(token: AccessToken)
}