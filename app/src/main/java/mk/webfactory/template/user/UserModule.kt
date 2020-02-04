package mk.webfactory.template.user

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.template.data.storage.FlatFileStorage
import mk.webfactory.template.data.storage.JsonConverter
import mk.webfactory.template.data.storage.JsonConverter.GsonConverter
import mk.webfactory.template.data.storage.Storage
import mk.webfactory.template.di.qualifier.ApplicationContext
import java.io.File
import java.lang.annotation.Documented
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class UserModule {
    @Qualifier
    @Documented
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class Internal

    private val userUpdateStream =
        BehaviorSubject.create<UserDataWrapper>()

    @Provides
    @Singleton
    fun provideFlatFileStorage(@ApplicationContext context: Context): Storage<UserDataWrapper> {
        val userFile =
            File(context.filesDir, GlobalConfig.USER_DATA_FILE)
        val jsonConverter: JsonConverter = GsonConverter(Gson())
        return FlatFileStorage(
            UserDataWrapper::class.java,
            userFile,
            jsonConverter
        )
    }

    @Provides
    @Named("user_update_stream")
    fun provideUserUpdateStream(): Observable<UserDataWrapper> {
        return userUpdateStream.hide()
    }

    @Provides
    @Internal
    fun provideUserUpdateStreamSource(): BehaviorSubject<UserDataWrapper> {
        return userUpdateStream
    }
}