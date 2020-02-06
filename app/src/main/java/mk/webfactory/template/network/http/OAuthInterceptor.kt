/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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