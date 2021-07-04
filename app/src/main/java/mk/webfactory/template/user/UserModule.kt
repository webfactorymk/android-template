package mk.webfactory.template.user

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import mk.webfactory.storage.FlatFileStorage
import mk.webfactory.storage.JsonConverter
import mk.webfactory.storage.Storage
import mk.webfactory.storage.StorageCache
import mk.webfactory.template.config.USER_DATA_FILE
import mk.webfactory.template.di.UserScopeComponentManager
import mk.webfactory.template.di.UserScopeEventHook
import mk.webfactory.template.di.qualifier.Internal
import mk.webfactory.template.model.user.User
import mk.webfactory.template.model.user.UserSession
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UserModule {

    @Provides
    @Singleton
    @Internal
    fun provideUserUpdatesSubject(): BehaviorSubject<UserSession> {
        return BehaviorSubject.create()
    }

    @Provides
    @Singleton
    @Named("user_updates")
    fun provideUserUpdates(@Internal updateStream: BehaviorSubject<UserSession>)
            : Observable<UserSession> {
        return UserManager.hideUpdatesStream(updateStream)
    }

    @Provides
    @Singleton
    fun provideUserStorage(@ApplicationContext context: Context, gson: Gson)
            : Storage<UserSession> {
        //todo provide keystore encrypted storage
        return StorageCache(FlatFileStorage(
            UserSession::class.java,
            File(context.filesDir, USER_DATA_FILE),
            JsonConverter.GsonConverter(gson)
        ))
    }

    @Provides
    @ElementsIntoSet
    fun provideDefaultUserEventHookSet(): Set<UserEventHook<User>> = emptySet()

    @Provides
    @IntoSet
    fun provideUserScopeEventHook(userScopeManager: UserScopeComponentManager) =
        UserScopeEventHook(userScopeManager)
}