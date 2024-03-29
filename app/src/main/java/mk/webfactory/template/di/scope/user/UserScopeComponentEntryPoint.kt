package mk.webfactory.template.di.scope.user

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import mk.webfactory.template.data.repository.movie.MovieRepository

/**
 * Dagger hilt EntryPoint for the [UserScopeComponent]
 *
 * For more info see: [https://dagger.dev/hilt/entry-points.html]
 */
@InstallIn(UserScopeComponent::class)
@EntryPoint
interface UserScopeComponentEntryPoint {
    fun movieRepository(): MovieRepository
}
