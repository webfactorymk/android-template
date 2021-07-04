package mk.webfactory.template

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import mk.webfactory.template.di.scope.user.UserScopeComponentManager
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.CrashlyticsLogger
import mk.webfactory.template.log.DebugLogger
import mk.webfactory.template.model.user.User
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserManager
import mk.webfactory.template.util.ActivityLifeCallbacks
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    companion object {
        val CRASH_REPORT: CrashReportLogger by lazy { crashReportLogger }
        private lateinit var crashReportLogger: CrashReportLogger
    }

    @Inject
    lateinit var userScopeComponentManager: UserScopeComponentManager

    @Inject
    lateinit var userManager: UserManager<UserSession>

    private val activityLifeCallbacks = object : ActivityLifeCallbacks() {

        override fun onApplicationEnteredForeground() {
            Timber.d("Application entered foreground")
            //TODO: Actions when application enters foreground
        }

        override fun onApplicationEnteredBackground() {
            Timber.d("Application entered background")

        }
    }

    private val userScopeMonitorListener = object : UserScopeComponentManager.Listener {

        override fun onUserScopeCreated(user: User) {
            Timber.d("UserScope created")
            CRASH_REPORT.setLoggedInUser(user.id)
        }

        override fun onUserScopeDestroyed() {
            Timber.d("UserScope destroyed")
            CRASH_REPORT.setLoggedInUser("anonymous")
        }
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { Timber.e(it) }
        crashReportLogger = initializeLoggingEnvironment()
        userScopeComponentManager.addUserScopeListener(userScopeMonitorListener)
        userManager.preloadUser().blockingAwait()
        registerActivityLifecycleCallbacks(activityLifeCallbacks)
    }

    private fun initializeLoggingEnvironment(): CrashReportLogger {
        val logger: CrashReportLogger =
            if (BuildConfig.DEBUG) {
                DebugLogger()
            } else {
                CrashlyticsLogger(this, FirebaseCrashlytics.getInstance())
            }

        Timber.plant(logger as Timber.Tree)
        return logger
    }
}