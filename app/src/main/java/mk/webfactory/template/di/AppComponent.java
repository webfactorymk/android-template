package mk.webfactory.template.di;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;
import mk.webfactory.template.App;
import mk.webfactory.template.data.DataModule;
import mk.webfactory.template.network.NetworkModule;
import mk.webfactory.template.network.api.ServiceModule;
import mk.webfactory.template.user.UserManager;
import mk.webfactory.template.user.UserModule;

@Singleton
@Component(modules = {
    AppModule.class,
    NetworkModule.class,
    DataModule.class,
    ServiceModule.class,
    UserModule.class,
    GlobalBindingModule.class,
    AndroidSupportInjectionModule.class
})
public interface AppComponent extends AndroidInjector<App> {

  UserScopeComponent.Builder userScopeComponentBuilder();

  UserManager getUserManager();

  void inject(App app);

  @Component.Builder interface Builder {

    @BindsInstance AppComponent.Builder application(Application application);

    AppComponent build();
  }
}
