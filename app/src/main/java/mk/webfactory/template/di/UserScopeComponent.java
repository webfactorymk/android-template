package mk.webfactory.template.di;

import android.app.Activity;
import dagger.BindsInstance;
import dagger.Subcomponent;
import dagger.android.DispatchingAndroidInjector;
import mk.webfactory.template.di.scope.UserScope;
import mk.webfactory.template.feature.home.HomeRepositoryModule;
import mk.webfactory.template.user.User;
import mk.webfactory.template.user.UserManager;

@UserScope
@Subcomponent(modules = {
    UserBindingModule.class,
    HomeRepositoryModule.class
    //Insert user scoped modules here (example: MockUserDataModule.class)
})
public interface UserScopeComponent {

  DispatchingAndroidInjector<Activity> getActivityInjector();

  void inject(UserManager userManager);

  @Subcomponent.Builder interface Builder {

    UserScopeComponent build();

    @BindsInstance Builder user(User user);
  }
}
