package mk.webfactory.template.model.auth

import com.google.gson.annotations.SerializedName

val VOID_ACCESS_TOKEN = AccessToken("-1", "nil", -1, 0)

data class AccessToken(
    @SerializedName("access_token") val token: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("expires_in") val expiresIn: Long
)