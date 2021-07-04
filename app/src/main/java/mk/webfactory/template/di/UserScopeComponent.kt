package mk.webfactory.template.di

import dagger.BindsInstance
import dagger.Subcomponent
import mk.webfactory.template.di.scope.UserScope
import mk.webfactory.template.feature.home.HomeRepositoryModule
import mk.webfactory.template.model.user.User

@UserScope
@Subcomponent(
    modules = [
        HomeRepositoryModule::class
        //TODO: Insert user scoped modules here (example: MockUserDataModule.class)
    ]
)
interface UserScopeComponent {

    val androidInjector: DispatchingAndroidInjector<Any>

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun user(user: User): Builder

        fun build(): UserScopeComponent
    }
}