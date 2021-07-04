package mk.webfactory.template.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mk.webfactory.template.di.scope.user.UserScopeComponent

@InstallIn(SingletonComponent::class)
@Module(
    subcomponents = [
        UserScopeComponent::class,
    ]
)
class AppSubcomponents