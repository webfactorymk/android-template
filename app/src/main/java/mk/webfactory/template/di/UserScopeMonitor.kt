package mk.webfactory.template.di

import io.reactivex.disposables.Disposable
import mk.webfactory.template.App
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.model.user.User
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserManager

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
class UserScopeMonitor(
    private val userManager: UserManager<UserSession>,
    private val appComponent: AppComponent
) {

    interface Listener {
        fun onUserSessionChanged(userSession: UserSession)
    }

    var userScopeComponent: UserScopeComponent? = null
        private set
    private var userUpdatesDisposable: Disposable? = null
    private var listener: Listener? = null


    /**
     * Start monitoring user session state. Also checks the last known value.
     */
    fun init(listener: Listener? = null) {
        this.listener = listener

        //We need it blocking for the first value if the user is logged in
        // so we can create the component immediately
        if (userManager.isLoggedIn()) {
            createUserScopeComponent(userManager.getLoggedInUserBlocking()!!.user)
        }
        userUpdatesDisposable.safeDispose()
        userUpdatesDisposable = userManager.updates()
            .skip(if (userManager.isLoggedIn()) 1 else 0)
            .distinctUntilChanged { lastState, newState ->
                lastState.isActive == newState.isActive
            }
            .doOnNext { listener?.onUserSessionChanged(it) }
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
        listener = null
    }

    private fun createUserScopeComponent(user: User) {
        userScopeComponent = appComponent.userScopeComponentBuilder()
            .user(user)
            .build()
    }
}