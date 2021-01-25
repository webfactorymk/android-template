package mk.webfactory.template.network.http

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import mk.webfactory.template.network.UnauthorizedUserException
import mk.webfactory.template.network.UnauthorizedUserHandler
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Type

class ErrorInterceptor(
    private val unauthorizedUserHandler: UnauthorizedUserHandler,
    private val gson: Gson
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return when (response.code) {
            400 -> handleBadRequest(response)
            401, 403 -> handleUnauthorizedRequest(chain.request(), response)
            else -> response
        }
    }

    private fun handleBadRequest(response: Response): Response {
        return try {
            val stringType: Type = object : TypeToken<Map<String, String>>() {}.type
            val apiError: Map<String, String> = gson.fromJson(response.body?.string(), stringType)
            throw ApiErrorException(response.code, apiError)
        } catch (e: JsonSyntaxException) {
            response
        }
    }

    private fun handleUnauthorizedRequest(request: Request, response: Response): Response {
        val unauthorizedUserException = UnauthorizedUserException(
            "URL: ${request.url} \nMessage: ${response.message}"
        )
        unauthorizedUserHandler.onUnauthorizedUserException(unauthorizedUserException)
        throw unauthorizedUserException
    }
}