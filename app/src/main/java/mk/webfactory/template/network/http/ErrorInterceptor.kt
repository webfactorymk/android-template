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
        return when (response.code()) {
            400 -> handleBadRequest(response)
            401, 403 -> handleUnauthorizedRequest(chain.request(), response)
            else -> response
        }
    }

    private fun handleBadRequest(response: Response): Response {
        return try {
            val stringType: Type = object : TypeToken<Map<String, String>>() {}.type
            val apiError: Map<String, String> = gson.fromJson(response.body()?.string(), stringType)
            throw ApiErrorException(response.code(), apiError)
        } catch (e: JsonSyntaxException) {
            response
        }
    }

    private fun handleUnauthorizedRequest(request: Request, response: Response): Response {
        val unauthorizedUserException = UnauthorizedUserException(
            "URL: ${request.url()} \nMessage: ${response.message()}"
        )
        unauthorizedUserHandler.onUnauthorizedUserException(unauthorizedUserException)
        throw unauthorizedUserException
    }
}