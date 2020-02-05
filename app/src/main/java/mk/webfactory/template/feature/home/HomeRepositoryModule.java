package mk.webfactory.template.feature.home;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import mk.webfactory.template.data.storage.InMemoryStorage;
import mk.webfactory.template.di.qualifier.Local;
import mk.webfactory.template.di.qualifier.Remote;
import mk.webfactory.template.network.api.UserService;
import mk.webfactory.template.user.BaseUser;

@Module
public class HomeRepositoryModule {

  @Provides
  @Named("userId")
  String provideUserId(BaseUser user) {
    return user.getId();
  }

  @Provides
  @Local
  HomeDataSource provideHomeLocalDataSource(@Named("userId") String userId) {
    return new HomeLocalDataSource(userId, new InMemoryStorage<>());
  }

  @Provides
  @Remote
  HomeDataSource provideHomeRemoteDataSource(@Named("userId") String userId, UserService userService) {
    return new HomeRemoteDataSource(userId, userService);
  }
}
