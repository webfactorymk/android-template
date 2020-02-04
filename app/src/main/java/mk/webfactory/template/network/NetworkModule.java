package mk.webfactory.template.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import javax.inject.Named;
import javax.inject.Singleton;
import mk.webfactory.template.BuildConfig;
import mk.webfactory.template.network.gson.ZonedDateTimeTypeAdapter;
import mk.webfactory.template.user.UserDataWrapper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.threeten.bp.ZonedDateTime;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

@Module
public class NetworkModule {

  @Provides @Singleton Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
        .create();
  }

  @Provides @Singleton OkHttpClient provideOkHttp(@Named("OAuth") Interceptor authInterceptor) {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);

    return new OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .addInterceptor(new ErrorInterceptor())
        .build();
  }

  @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client, Gson gson, @Named("base.url") String baseUrl) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @Provides @Named("base.url") String provideBaseUrl() {
    return BuildConfig.API_BASE_URL;
  }

  @Provides @Singleton @Named("OAuth")
  Interceptor provideOAuthInterceptor(@Named("user_update_stream") Observable<UserDataWrapper> userUpdateStream) {
    OAuthInterceptor authInterceptor = new OAuthInterceptor();
    authInterceptor.setUserUpdateStream(userUpdateStream);
    return authInterceptor;
  }

  //Use this if you have a refresh token and inject a mechanism for updating user manager w/ the new access token
  //@Provides @Singleton @Named("OAuth") Authenticator provideOAuthAuthenticator() {}
}
