package mk.webfactory.template.user

import io.reactivex.Single
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.network.OAuthInterceptor
import mk.webfactory.template.network.api.UserService


class OAuthProvider(
    private val service: UserService,
    private val requestInterceptor: OAuthInterceptor,
    private val token: AccessToken
) : AuthProvider<BaseUser> {
    override fun login(): Single<BaseUser> {
        return service.login()
    }

    override fun logout(token: AccessToken) {
        service.logout()
    }
}
