package mk.webfactory.template.network.http

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * [Interceptor] that adds TMDB (themoviedb.org) api key query string to each request.
 */
class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        val newRequestUrl = oldRequest.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val newRequest = oldRequest.newBuilder()
            .url(newRequestUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
