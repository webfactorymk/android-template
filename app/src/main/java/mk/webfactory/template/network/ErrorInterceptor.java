package mk.webfactory.template.network;

import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ErrorInterceptor implements Interceptor {

  @Override public Response intercept(@NonNull Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    if (response.code() == 401) {
      throw new UnauthorizedUserException("URL: " + chain.request().url().toString()
          + "\nMessage: " + response.message());
    } else {
      return response;
    }
  }
}