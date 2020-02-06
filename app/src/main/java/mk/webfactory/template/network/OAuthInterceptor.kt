package mk.webfactory.template.network

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.UserSession
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

/**
 *
 * [Interceptor] that adds OAuth2 authorization header on each request.
 *
 * Mind to update the token value when it changes.
 * Setting it to `null` disables this interceptor.
 */

class OAuthInterceptor() : Interceptor {
    @get:Synchronized private var accessToken: AccessToken? = null
    private lateinit var userUpdateStreamDisposable: Disposable

    fun setUserUpdateStream(userUpdateStream: Observable<UserSession?>) {
        dispose(AtomicReference(userUpdateStreamDisposable))
        userUpdateStreamDisposable = userUpdateStream
            .map { userSession -> userSession.accessToken }
            .subscribe { t ->
                if (t != accessToken) {
                    updateAccessToken(t)
                }
            }
    }

    @Synchronized
    private fun updateAccessToken(accessToken: AccessToken?) {
        this.accessToken = accessToken
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