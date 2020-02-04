package mk.webfactory.template.di;

import android.app.Activity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import javax.inject.Singleton;
import mk.webfactory.template.di.scope.ActivityScope;
import mk.webfactory.template.feature.launcher.ui.LauncherActivity;
import mk.webfactory.template.feature.login.ui.LoginActivity;

/**
 * Binding of {@linkplain Activity Activities} within the {@linkplain Singleton Singleton/App} scope
 * i.e. screens independent of user/session data.
 *
 * @see UserBindingModule UserBindingModule for user scope
 */
@Module(subcomponents = UserScopeComponent.class)
public abstract class GlobalBindingModule {

  @ActivityScope
  @ContributesAndroidInjector()
  abstract LauncherActivity launcherActivity();

  @ActivityScope
  @ContributesAndroidInjector()
  abstract LoginActivity loginActivity();
}
