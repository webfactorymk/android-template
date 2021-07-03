package mk.webfactory.template.feature.home.ui

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import mk.webfactory.template.di.scope.ActivityScope
import mk.webfactory.template.di.scope.FragmentScope


@Module
abstract class HomeUiModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment?

    //todo include in details screen

    //@Provides
    //@ActivityScoped
    //static String provideTaskId(TaskDetailActivity activity) {
    //    return activity.getIntent().getStringExtra(EXTRA_TASK_ID);
    //}
}