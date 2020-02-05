package mk.webfactory.template.user

import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.network.OAuthInterceptor
import mk.webfactory.template.network.api.UserService

class OAuthProviderPrototype(
    private val service: UserService,
    private val interceptor: OAuthInterceptor
) {

    fun withToken(token: AccessToken) = OAuthProvider(service, interceptor, token)
}