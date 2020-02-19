package mk.webfactory.template.di

import mk.webfactory.template.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates and destroys the [UserScopeComponent], notifying all added listeners.
 *
 * The user scope component need to be created synchronously on the following events:
 * - on app start if there is a logged in user
 * - on user login
 *
 * and destroyed:
 * - on user logout
 */
@Singleton
class UserScopeCreator @Inject constructor(
    private val userScopeBuilder: UserScopeComponent.Builder
) {

    var userScopeComponent: UserScopeComponent? = null
        private set

    private var listeners: MutableSet<Listener> = LinkedHashSet()

    fun addUserScopeListener(listener: Listener) = listeners.add(listener)

    fun removeUserScopeListener(listener: Listener) = listeners.remove(listener)

    fun createUserScopeComponent(user: User) {
        if (userScopeComponent != null) {
            return
        }
        userScopeComponent = userScopeBuilder
            .user(user)
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