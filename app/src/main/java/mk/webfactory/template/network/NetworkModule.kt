package mk.webfactory.template.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Observable
import mk.webfactory.template.BuildConfig
import mk.webfactory.template.model.movie.Movie
import mk.webfactory.template.model.movie.Show
import mk.webfactory.template.model.movie.TvShow
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.network.gson.RuntimeTypeAdapterFactory
import mk.webfactory.template.network.gson.ZonedDateTimeTypeAdapter
import mk.webfactory.template.network.http.ApiKeyInterceptor
import mk.webfactory.template.network.http.ErrorInterceptor
import mk.webfactory.template.network.http.OAuthInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZonedDateTime
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                ZonedDateTime::class.java,
                ZonedDateTimeTypeAdapter()
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(Show::class.java, "media_type")
                    .apply {
                        registerSubtype(Movie::class.java, "movie")
                        registerSubtype(TvShow::class.java, "tv")
                    })
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        @Named("ApiKey") apiKeyInterceptor: Interceptor,
        @Named("OAuth") authInterceptor: Interceptor,
        @Named("Error") errorInterceptor: Interceptor,
        @Named("Logging") loggingInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("base.url") baseUrl: String,
        client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Named("base.url")
    fun provideBaseUrl(): String {
        return BuildConfig.API_BASE_URL
    }

    @Provides
    @Named("tmdb-api-key")
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Provides
    @Named("ApiKey")
    fun provideApiKeyInterceptor(@Named("tmdb-api-key") apiKey: String): Interceptor {
        return ApiKeyInterceptor(apiKey)
    }

    @Provides
    @Singleton
    @Named("OAuth")
    fun provideOAuthInterceptor(
        @Named("user_updates") userUpdateStream: Observable<UserSession>
    ): Interceptor {
        return OAuthInterceptor().apply { setUserUpdateStream(userUpdateStream) }
    }

    @Provides
    @Named("Error")
    fun provideErrorInterceptor(
        unauthorizedUserHandler: UnauthorizedUserHandlerImpl,
        gson: Gson
    ): Interceptor {
        return ErrorInterceptor(unauthorizedUserHandler, gson)
    }

    @Provides
    @Named("Logging")
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }
    }

    //Note: If you use REFRESH TOKEN add Authenticator to OkHttp
    // and update user manager w/ the new access token
    //@Provides @Singleton @Named("OAuth") fun provideOAuthAuthenticator(): Authenticator
}