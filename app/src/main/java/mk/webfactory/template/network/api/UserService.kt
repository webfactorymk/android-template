package mk.webfactory.template.network.api

import io.reactivex.Completable
import io.reactivex.Single
import mk.webfactory.template.network.api.body.DeviceFcmTokenBody
import mk.webfactory.template.user.BaseUser
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

interface UserService {
    //@PUT("user/{id}") todo
    fun updateDeviceToken(
        @Path("id") userId: String?,
        @Body fcmTokenBody: DeviceFcmTokenBody?
    ): Single<Response<ResponseBody>>

    //@GET("login") todo
    fun login(): Single<BaseUser>

    //@GET("auth/logout") todo
    fun logout(): Completable?
}