package mk.webfactory.template.user

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.template.config.USER_DATA_FILE
import mk.webfactory.template.data.storage.FlatFileStorage
import mk.webfactory.template.data.storage.JsonConverter
import mk.webfactory.template.data.storage.JsonConverter.GsonConverter
import mk.webfactory.template.data.storage.Storage
import mk.webfactory.template.di.qualifier.ApplicationContext
import mk.webfactory.template.di.qualifier.Internal
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class UserModule {

    private val userUpdateStream = BehaviorSubject.create<UserSession>()

    @Provides
    @Internal
    fun provideUserUpdateStreamSource(): BehaviorSubject<UserSession> {
        return userUpdateStream
    }

    @Provides
    @Singleton
    fun provideFlatFileStorage(@ApplicationContext context: Context): Storage<UserSession> {
        val userFile =
            File(context.filesDir, USER_DATA_FILE)
        val jsonConverter: JsonConverter = GsonConverter(Gson())
        return FlatFileStorage(
            UserSession::class.java,
            userFile,
            jsonConverter
        )
    }

    @Provides
    @Singleton
    fun provideUserManager(
        @Internal userUpdates: BehaviorSubject<UserSession>,
        userStorage: Storage<UserSession>
    ): UserManager<UserSession> {
        return UserManager(userUpdates, userStorage)
    }
}