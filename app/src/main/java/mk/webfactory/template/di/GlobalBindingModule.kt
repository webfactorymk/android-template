package mk.webfactory.template.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mk.webfactory.template.di.scope.ActivityScope
import mk.webfactory.template.feature.launcher.ui.LauncherActivity
import mk.webfactory.template.feature.login.ui.LoginActivity

/**
 * Binding of [Activities][Activity] within the [Singleton/App][Singleton] scope,
 * but outside of [UserScope]. i.e. screens independent of user/session data.
 *
 * @see UserBindingModule UserBindingModule for user scope
 */
@Module(subcomponents = [UserScopeComponent::class])
abstract class GlobalBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun launcherActivity(): LauncherActivity?

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity?
}