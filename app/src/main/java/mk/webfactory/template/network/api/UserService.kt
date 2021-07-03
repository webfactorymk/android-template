package mk.webfactory.template.network.api

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mk.webfactory.template.model.user.User
import mk.webfactory.template.model.fcm.DeviceFcmTokenBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    //TODO: Provide real endpoint data

    @PUT("user/{id}")
    fun updateFcmDeviceToken(
        @Path("id") userId: String,
        @Body fcmTokenBody: DeviceFcmTokenBody
    ): Single<Response<ResponseBody>>

    @POST("auth/login")
    fun login(): Single<User>

    @POST("auth/logout")
    fun logout(): Completable
}