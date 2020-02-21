package mk.webfactory.template.user

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.storage.FlatFileStorage
import mk.webfactory.storage.JsonConverter
import mk.webfactory.storage.Storage
import mk.webfactory.template.config.USER_DATA_FILE
import mk.webfactory.template.di.UserScopeCreator
import mk.webfactory.template.di.qualifier.ApplicationContext
import mk.webfactory.template.di.qualifier.Internal
import mk.webfactory.template.model.user.User
import mk.webfactory.template.model.user.UserSession
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class UserModule {

    @Provides
    @Singleton
    @Internal
    fun provideUserUpdatesSubject(): BehaviorSubject<UserSession> {
        return BehaviorSubject.create<UserSession>()
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
    fun provideFlatFileStorage(@ApplicationContext context: Context, gson: Gson)
            : Storage<UserSession> {
        return FlatFileStorage(
                UserSession::class.java,
                File(context.filesDir, USER_DATA_FILE),
                JsonConverter.GsonConverter(gson)
        )
    }

    @Provides
    @ElementsIntoSet
    fun provideDefaultLogoutHookSet(): Set<LogoutHook> = emptySet()

    @Provides
    @ElementsIntoSet
    fun provideDefaultLoginHookSet(): Set<LoginHook<User>> = emptySet()

    @Provides
    @IntoSet
    fun provideUserScopeLoginHook(userScopeCreator: UserScopeCreator): LoginHook<UserSession> {
        return object : LoginHook<UserSession> {
            override fun postLogin(userSession: UserSession) =
                    Completable.fromAction { userScopeCreator.createUserScopeComponent(userSession.user) }
        }
    }

    @Provides
    @IntoSet
    fun provideUserScopeLogoutHook(userScopeCreator: UserScopeCreator): LogoutHook {
        return object : LogoutHook {
            override fun postLogout() =
                    Completable.fromAction { userScopeCreator.destroyUserScopeComponent() }
        }
    }
}