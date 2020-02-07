package mk.webfactory.template.feature.home

import dagger.Module
import dagger.Provides
import mk.webfactory.storage.InMemoryStorage
import mk.webfactory.template.di.qualifier.Local
import mk.webfactory.template.di.qualifier.Remote
import mk.webfactory.template.model.user.User
import mk.webfactory.template.network.api.UserService
import javax.inject.Named

@Module
class HomeRepositoryModule {
    @Provides
    @Named("userId")
    fun provideUserId(user: User): String {
        return user.id
    }

    @Provides
    @Local
    fun provideHomeLocalDataSource(@Named("userId") userId: String): HomeDataSource {
        return HomeLocalDataSource(userId, InMemoryStorage())
    }

    @Provides
    @Remote
    fun provideHomeRemoteDataSource(
        @Named("userId") userId: String, userService: UserService
    ): HomeDataSource {
        return HomeRemoteDataSource(userId, userService)
    }
}