package mk.webfactory.template.network

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.user.UserDataWrapper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

internal class OAuthInterceptor(accessToken: AccessToken?) : Interceptor {
    @get:Synchronized
    private var accessToken: AccessToken? = accessToken
    private lateinit var userUpdateStreamDisposable: Disposable

    fun setUserUpdateStream(@NonNull userUpdateStream: Observable<UserDataWrapper?>) {
        dispose(AtomicReference(userUpdateStreamDisposable))
        userUpdateStreamDisposable = userUpdateStream
            .map { userDataWrapper -> userDataWrapper.accessToken }
            .subscribe { t ->
                if (t != accessToken) {
                    updateAccessToken(t)
                }
            }
    }

    @Synchronized
    private fun updateAccessToken(@Nullable accessToken: AccessToken?) {
        this.accessToken = accessToken
    }

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
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