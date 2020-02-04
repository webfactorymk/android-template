package mk.webfactory.template.di;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import mk.webfactory.template.di.qualifier.ApplicationContext;

@Module
public class AppModule {

  @Provides @ApplicationContext public Context provideContext(Application application) {
    return application;
  }
}
