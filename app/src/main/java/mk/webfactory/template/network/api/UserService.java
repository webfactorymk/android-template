package mk.webfactory.template.network.api;

import io.reactivex.Completable;
import io.reactivex.Single;
import mk.webfactory.template.user.User;
import mk.webfactory.template.network.api.body.DeviceFcmTokenBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface UserService {

  //@PUT("user/{id}") todo
  Single<Response<ResponseBody>> updateDeviceToken(
      @Path("id") String userId,
      @Body DeviceFcmTokenBody fcmTokenBody);

  //@GET("login") todo
  Single<User> login();

  //@GET("auth/logout") todo
  Completable logout();
}
