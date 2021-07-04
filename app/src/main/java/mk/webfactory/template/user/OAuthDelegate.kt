package mk.webfactory.template.user

import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.network.api.UserService
import mk.webfactory.template.network.http.OAuthInterceptor
import javax.inject.Inject

class OAuthDelegateProvider @Inject constructor(
        private val service: UserService,
        private val interceptor: OAuthInterceptor
) {

    fun withToken(token: AccessToken) = OAuthDelegate(service, interceptor, token)
}

class OAuthDelegate(
        private val userService: UserService,
        private val requestInterceptor: OAuthInterceptor,
        private val token: AccessToken
) : AuthDelegate<UserSession> {

    override fun login(): Single<UserSession> {
        return userService.login().map { user -> UserSession(user, token) }
    }

    override fun logout(user: UserSession): Single<UserSession> {
        return userService.logout()
                .andThen(Single.just(user.copy(accessToken = null)))
    }
}
