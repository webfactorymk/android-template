package mk.webfactory.template.data.repository.movie

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import mk.webfactory.template.di.qualifier.Local
import mk.webfactory.template.di.qualifier.Remote
import mk.webfactory.template.di.scope.user.UserScopeComponent
import mk.webfactory.template.model.user.User
import mk.webfactory.template.network.api.MovieApiService
import javax.inject.Named

@InstallIn(UserScopeComponent::class)
@Module
class MovieRepositoryModule {
    @Provides
    @Named("userId")
    fun provideUserId(user: User): String {
        return user.id
    }

    @Provides
    @Local
    fun provideHomeLocalDataSource(@Named("userId") userId: String): MovieDataSource {
        return MovieCacheDataSource(userId)
    }

    @Provides
    @Remote
    fun provideHomeRemoteDataSource(
        @Named("userId") userId: String, movieApiService: MovieApiService
    ): MovieDataSource {
        return MovieRemoteDataSource(userId, movieApiService)
    }
}