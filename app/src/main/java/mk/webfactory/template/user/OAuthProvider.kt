package mk.webfactory.template.user


import io.reactivex.Single
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.network.api.UserService


class OAuthProvider(service: UserService) : AuthProvider<User> {
    var apiService: UserService = service
    override fun login(token: AccessToken): Single<User> {
        return apiService.login()
    }

    override fun logout(token: AccessToken) {
        apiService.logout()
    }
}
