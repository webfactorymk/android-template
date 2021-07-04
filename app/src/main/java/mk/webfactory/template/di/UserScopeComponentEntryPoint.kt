package mk.webfactory.template.di

import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import mk.webfactory.template.feature.home.HomeRepository

/**
 * Dagger hilt EntryPoint for the [UserScopeComponent]
 *
 * For more info see: [https://dagger.dev/hilt/entry-points.html]
 */
@InstallIn(UserScopeComponent::class)
@EntryPoint
interface UserScopeComponentEntryPoint {
    fun homeRepository(): HomeRepository
}
