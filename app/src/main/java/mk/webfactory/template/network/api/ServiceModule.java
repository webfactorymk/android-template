package mk.webfactory.template.network.api;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module public class ServiceModule {

  @Provides @Singleton public UserService provideUserService(Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }
}