package mk.webfactory.template.di

import dagger.BindsInstance
import dagger.hilt.DefineComponent
import mk.webfactory.template.di.scope.UserScope

@UserScope
@DefineComponent(parent = ApplicationComponent::class)
interface UserScopeComponent {

    @DefineComponent.Builder
    interface Builder {
        fun setUser(@BindsInstance userId: String): UserScopeComponent.Builder
        fun build(): UserScopeComponent
    }
}