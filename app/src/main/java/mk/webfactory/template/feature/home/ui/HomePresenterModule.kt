package mk.webfactory.template.feature.home.ui

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import mk.webfactory.template.di.scope.ActivityScope
import mk.webfactory.template.di.scope.FragmentScope
import mk.webfactory.template.feature.home.ui.mvp.HomeContract
import mk.webfactory.template.feature.home.ui.mvp.HomeFragment
import mk.webfactory.template.feature.home.ui.mvp.HomePresenter

@Module
abstract class HomePresenterModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment?

    @ActivityScope
    @Binds
    abstract fun homePresenter(presenter: HomePresenter?): HomeContract.Presenter? //todo include in details screen
//@Provides
//@ActivityScoped
//static String provideTaskId(TaskDetailActivity activity) {
//    return activity.getIntent().getStringExtra(EXTRA_TASK_ID);
//}
}