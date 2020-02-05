package mk.webfactory.template.di

import io.reactivex.disposables.Disposable
import mk.webfactory.template.App
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.user.User
import mk.webfactory.template.user.UserManager
import mk.webfactory.template.user.UserSession
import javax.inject.Inject

/**
 * Monitors the [UserSession] state and creates and tears down the [UserScopeComponent].
 */
class UserScopeMonitor @Inject constructor(
    private val userManager: UserManager<UserSession>,
    private val app: App
) {

    var userScopeComponent: UserScopeComponent? = null
        private set
    private var userUpdatesDisposable: Disposable? = null


    /**
     * Start monitoring user session state. Also checks the last known value.
     */
    fun init() {
        //We need it blocking for the first value if the user is logged in
        // so we can create the component immediately
        if (userManager.isLoggedIn()) {
            createUserScopeComponent(userManager.getLoggedInUser().blockingFirst().user)
        }
        userUpdatesDisposable.safeDispose()
        userUpdatesDisposable = userManager.updates()
            .skip(if (userManager.isLoggedIn()) 1 else 0)
            .distinctUntilChanged { lastState, newState ->
                lastState.isActive == newState.isActive
            }
            .subscribe { session ->
                if (session.isActive) {
                    createUserScopeComponent(session.user)
                } else {
                    userScopeComponent = null
                }
            }
    }

    private fun createUserScopeComponent(user: User) {
        userScopeComponent = app.appComponent.userScopeComponentBuilder()
            .user(user)
            .build()
    }
}