package mk.webfactory.template.di.scope.user

import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@UserScope
@DefineComponent(parent = SingletonComponent::class)
interface UserScopeComponent {

    @DefineComponent.Builder
    interface Builder {
        fun setUser(@BindsInstance userId: String): Builder
        fun build(): UserScopeComponent
    }
}