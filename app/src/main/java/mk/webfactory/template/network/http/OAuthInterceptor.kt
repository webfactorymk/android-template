package mk.webfactory.template.network.http

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.UserSession
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * [Interceptor] that adds OAuth2 authorization header on each request.
 *
 * Mind to update the token value when it changes.
 * Setting it to `null` disables this interceptor.
 */
class OAuthInterceptor : Interceptor {

    var accessToken: AccessToken? = null
        @Synchronized get
        @Synchronized set

    private var userUpdateStreamDisposable: Disposable? = null

    fun setUserUpdateStream(userUpdatesStream: Observable<UserSession>) {
        userUpdateStreamDisposable.safeDispose()
        userUpdateStreamDisposable = userUpdatesStream
            .subscribe { session ->
                accessToken = if (session.isActive) session.accessToken else null
            }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = accessToken
        return if (accessToken != null) {
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer " + accessToken.token)
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }
}