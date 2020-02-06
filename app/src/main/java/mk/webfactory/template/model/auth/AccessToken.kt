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

package mk.webfactory.template.model.auth

import com.google.gson.annotations.SerializedName


class AccessToken(
    accessToken: String?,
    tokenType: String?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("expires_in") val expiresIn: Long
) {
    @SerializedName("access_token")
    val token: String = checkNotNull(accessToken)
    @SerializedName("token_type")
    val tokenType: String = checkNotNull(tokenType)

    //system time can be manipulated by changing device time, but then the request will fail so it's OK
    val isTokenExpired: Boolean
        get() = createdAt != 0L && System.currentTimeMillis() > createdAt + expiresIn

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as AccessToken
        if (createdAt != that.createdAt) {
            return false
        }
        if (expiresIn != that.expiresIn) {
            return false
        }
        return if (token != that.token) {
            false
        } else tokenType == that.tokenType
    }

    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + tokenType.hashCode()
        result = 31 * result + (createdAt xor (createdAt ushr 32)).toInt()
        result = 31 * result + (expiresIn xor (expiresIn ushr 32)).toInt()
        return result
    }

    override fun toString(): String {
        return javaClass.simpleName + "[" +
                "token=" + token + ", " +
                "tokenType=" + tokenType + ", " +
                "createdAt=" + createdAt + ", " +
                "expiresIn=" + expiresIn + ", " +
                "isExpired=" + isTokenExpired + "]"
    }

    companion object {
        val VOID = AccessToken("-1", "nil", -1, 0)
    }

}