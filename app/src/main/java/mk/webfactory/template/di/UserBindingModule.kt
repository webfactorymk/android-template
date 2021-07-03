package mk.webfactory.template.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mk.webfactory.template.di.scope.ActivityScope
import mk.webfactory.template.feature.home.ui.HomeActivity
import mk.webfactory.template.feature.home.ui.HomeUiModule

/**
 * Binding of [Activities][Activity] within the [UserScope]
 * i.e. screens requiring logged in user to display user specific data.
 *
 * @see GlobalBindingModule AppBindingModule for global scope
 */
@Module
abstract class UserBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeUiModule::class])
    abstract fun homeActivity(): HomeActivity?
}