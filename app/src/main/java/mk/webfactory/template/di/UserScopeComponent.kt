package mk.webfactory.template.di

import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.android.components.ApplicationComponent
import mk.webfactory.template.di.scope.UserScope
import mk.webfactory.template.model.user.User

@UserScope
@DefineComponent(parent = ApplicationComponent::class)
interface UserScopeComponent {

    @DefineComponent.Builder
    interface Builder {
        fun setUser(@BindsInstance user: User): UserScopeComponent.Builder
        fun build(): UserScopeComponent
    }
}