package mk.webfactory.template.di.scope.user

import dagger.hilt.EntryPoints
import mk.webfactory.template.model.user.User
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Manages the [UserScopeComponent]
 *
 * - holds an instance
 * - offers dagger hilt entry point
 * - offers create and destroy methods
 * - offers listeners for create and destroy events
 */
@Singleton
class UserScopeComponentManager @Inject constructor(
    private val userScopeComponentProvider: Provider<UserScopeComponent.Builder>
) {

    @Volatile
    var userId: String? = null
        private set

    var userScopeComponent: UserScopeComponent? = null
        @Synchronized get
        private set

    val entryPoint: UserScopeComponentEntryPoint
        get() = EntryPoints.get(userScopeComponent!!, UserScopeComponentEntryPoint::class.java)

    private var listeners: MutableSet<Listener> = LinkedHashSet()

    fun addUserScopeListener(listener: Listener) = listeners.add(listener)

    fun removeUserScopeListener(listener: Listener) = listeners.remove(listener)

    @Synchronized
    fun isUserScopeComponentCreated() = userScopeComponent != null

    @Synchronized
    fun createUserScopeComponent(user: User) {
        if (userScopeComponent != null) {
            //will be recreated
        }
        userId = user.id
        userScopeComponent = userScopeComponentProvider.get()
            .setUser(user)
            .build()
        listeners.forEach { it.onUserScopeCreated(user) }
    }

    @Synchronized
    fun destroyUserScopeComponent() {
        if (userScopeComponent == null) {
            return
        }
        userId = null
        userScopeComponent = null
        listeners.forEach { it.onUserScopeDestroyed() }
    }

    interface Listener {

        fun onUserScopeCreated(user: User)

        fun onUserScopeDestroyed()
    }
}