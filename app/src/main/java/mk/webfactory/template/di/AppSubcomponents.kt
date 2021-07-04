package mk.webfactory.template.di

import dagger.Module
import dagger.hilt.InstallIn

@InstallIn(SingletonComponent::class)
@Module(
    subcomponents = [
        UserScopeComponent::class,
    ]
)
class AppSubcomponents