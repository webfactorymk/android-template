package mk.webfactory.template.user

import io.reactivex.Single
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.network.api.UserService
import mk.webfactory.template.network.http.OAuthInterceptor
import javax.inject.Inject

class OAuthProviderPrototype @Inject constructor(
    private val service: UserService,
    private val interceptor: OAuthInterceptor
) {

    fun withToken(token: AccessToken) = OAuthProvider(service, interceptor, token)
}

class OAuthProvider(
    private val userService: UserService,
    private val requestInterceptor: OAuthInterceptor,
    private val token: AccessToken
) : AuthProvider<UserSession> {

    override fun login(): Single<UserSession> {
        return userService.login().map { user -> UserSession(user, token,null) }
    }

    override fun logout() = userService.logout()
}
