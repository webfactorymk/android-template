package mk.webfactory.template.di.scope.user

import io.reactivex.rxjava3.core.Completable
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserEventHook
import timber.log.Timber

/**
 * Monitors user events and creates and destroys the [UserScopeComponent] accordingly.
 *
 * __The user scope component lifecycle:__
 * - created on
 *     - app start if there is a logged in user
 *     - user explicit login
 *
 * - destroyed on
 *     - user explicit logout
 *
 * - recreated on
 *     - new user login after session expiry
 *
 * - kept intact on
 *     - same user login after session expiry
 */
class UserScopeEventHook(private val userScopeComponentManager: UserScopeComponentManager) :
    UserEventHook<UserSession> {

    override fun postLogin(user: UserSession): Completable =
        Completable.fromAction {
            synchronized(this@UserScopeEventHook) {
                if (shouldCreateUserScope(user.user.id)) {
                    userScopeComponentManager.createUserScopeComponent(user.user)
                }
            }
        }

    override fun postLogout(): Completable =
        Completable.fromAction {
            synchronized(this@UserScopeEventHook) {
                userScopeComponentManager.destroyUserScopeComponent()
            }
        }

    override fun onUserLoaded(user: UserSession?): Completable =
        Completable.fromAction {
            if (user == null) {
                return@fromAction
            }
            synchronized(this@UserScopeEventHook) {
                if (shouldCreateUserScope(user.user.id)) {
                    userScopeComponentManager.createUserScopeComponent(user.user)
                }
            }
        }

    /**
     * Checks if the user scope exists and is configured correctly
     *
     * @return `true` if it should be created/recreated, `false` otherwise.
     * */
    private fun shouldCreateUserScope(userId: String): Boolean {
        return if (userScopeComponentManager.isUserScopeComponentCreated().not()) {
            Timber.d("UserScope - Not created: Should be created.")
            true
        } else {
            val prevUserId = userScopeComponentManager.userId
            if (prevUserId == userId) {
                Timber.d("UserScope - Exists: The same user logins after session expired.")
                false
            } else {
                Timber.d("UserScope - Outdated: NEW user logins after session expired.")
                true
            }
        }
    }
}