package mk.webfactory.template.user

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.storage.FlatFileStorage
import mk.webfactory.storage.JsonConverter
import mk.webfactory.storage.Storage
import mk.webfactory.template.config.USER_DATA_FILE
import mk.webfactory.template.di.qualifier.ApplicationContext
import mk.webfactory.template.model.user.UserSession
import java.io.File
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class UserModule {
    @Qualifier
    @MustBeDocumented
    annotation class Internal

    @Provides
    @Internal
    fun provideUserUpdatesSubject(): BehaviorSubject<UserSession> {
        return BehaviorSubject.create<UserSession>()
    }

    @Provides
    @Named("user_updates")
    fun provideUserUpdates(@Internal updateStream: BehaviorSubject<UserSession>)
            : Observable<UserSession> {
        return UserManager.hideUpdatesStream(updateStream)
    }

    @Provides
    @Singleton
    fun provideFlatFileStorage(@ApplicationContext context: Context, gson: Gson)
            : Storage<UserSession> {
        return FlatFileStorage(
            UserSession::class.java,
            File(context.filesDir, USER_DATA_FILE),
            JsonConverter.GsonConverter(gson)
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