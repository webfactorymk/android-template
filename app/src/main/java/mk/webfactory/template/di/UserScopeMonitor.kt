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
 * Initialize in [App.onCreate] and fetch [userScopeComponent] (nullable) when needed.
 *
 * Example usage with dagger-android:
 * ```
 * class App : DaggerApplication() {
 *
 *     @Inject
 *     lateinit var userScopeMonitor: UserScopeMonitor
 *
 *      override fun onCreate() {
 *          super.onCreate()
 *          userScopeMonitor.init()
 *          // ...
 *      }
 *
 *     override fun androidInjector(): AndroidInjector<Any> {
 *         return userScopeMonitor.userScopeComponent?.androidInjector
 *             ?: super.androidInjector()
 *     }
 *
 *    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
 *        return DaggerAppComponent.builder().application(this).build()
 *            .also({ appComponent = it })
 *    }
 * }
 * ```
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
            createUserScopeComponent(userManager.getLoggedInUserBlocking().user)
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

    /**
     * Stops monitoring user's session state and destroys [userScopeComponent].
     */
    fun destroy() {
        userUpdatesDisposable.safeDispose()
        userScopeComponent = null
    }

    private fun createUserScopeComponent(user: User) {
        userScopeComponent = app.appComponent.userScopeComponentBuilder()
            .user(user)
            .build()
    }
}