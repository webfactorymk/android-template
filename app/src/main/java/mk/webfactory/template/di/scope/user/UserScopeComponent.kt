package mk.webfactory.template.di.scope.user

import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent
import mk.webfactory.template.model.user.User

@UserScope
@DefineComponent(parent = SingletonComponent::class)
interface UserScopeComponent {

    @DefineComponent.Builder
    interface Builder {
        fun setUser(@BindsInstance user: User): Builder
        fun build(): UserScopeComponent
    }
}