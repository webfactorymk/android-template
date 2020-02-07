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