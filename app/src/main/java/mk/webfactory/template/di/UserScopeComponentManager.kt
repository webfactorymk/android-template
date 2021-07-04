package mk.webfactory.template.di

import dagger.hilt.EntryPoints
import mk.webfactory.template.feature.home.HomeRepository
import mk.webfactory.template.model.user.User
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Creates and destroys the [UserScopeComponent], notifying all added listeners.
 *
 * The user scope component needs to be created synchronously on the following events:
 * - on app start if there is a logged in user
 * - on user login
 *
 * and destroyed:
 * - on user logout
 *
 * and recreated on:
 * - new user login after session expiry
 *
 * and kept intact on:
 * - same user login after session expiry
 */
@Singleton
class UserScopeComponentManager @Inject constructor(
    private val userScopeComponentProvider: Provider<UserScopeComponent.Builder>
) {

    var userScopeComponent: UserScopeComponent? = null
        private set

    val entryPoint: UserScopeComponentEntryPoint
        get() = EntryPoints.get(userScopeComponent!!, UserScopeComponentEntryPoint::class.java)

    private var listeners: MutableSet<Listener> = LinkedHashSet()

    fun addUserScopeListener(listener: Listener) = listeners.add(listener)

    fun removeUserScopeListener(listener: Listener) = listeners.remove(listener)

    fun createUserScopeComponent(user: User) {
        if (userScopeComponent != null) {
            return
        }
        userScopeComponent = userScopeComponentProvider.get()
            .setUser(user)
            .build()
        listeners.forEach { it.onUserScopeCreated(user) }
    }

    fun destroyUserScopeComponent() {
        userScopeComponent = null
        listeners.forEach { it.onUserScopeDestroyed() }
    }

    interface Listener {

        fun onUserScopeCreated(user: User)

        fun onUserScopeDestroyed()
    }
}