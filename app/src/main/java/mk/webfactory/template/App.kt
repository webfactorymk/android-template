package mk.webfactory.template

import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import mk.webfactory.template.di.AppComponent
import mk.webfactory.template.di.DaggerAppComponent
import mk.webfactory.template.di.UserScopeMonitor
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.CrashlyticsLogger
import mk.webfactory.template.log.DebugLogger
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.util.ActivityLifeCallbacks
import timber.log.Timber
import javax.inject.Inject

class App : DaggerApplication() {

    //FIXME: use the crash report logger when user is loaded and screen resumed
    companion object {
        val CRASH_REPORT: CrashReportLogger by lazy { crashReportLogger }
        private lateinit var crashReportLogger: CrashReportLogger
    }

    @Inject
    lateinit var userScopeMonitor: UserScopeMonitor
    lateinit var appComponent: AppComponent

    private val activityLifeCallbacks = object : ActivityLifeCallbacks() {

        override fun onApplicationEnteredForeground() {
            //TODO: Actions when application enters foreground
        }

        override fun onApplicationEnteredBackground() {}
    }

    private val userSessionChangeListener = object : UserScopeMonitor.Listener {

        override fun onUserSessionChanged(userSession: UserSession) {
            CRASH_REPORT.setLoggedInUser(userSession.user.id + " (active=${userSession.isActive})")
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        crashReportLogger = initializeLoggingEnvironment()
        userScopeMonitor.init(userSessionChangeListener)
        registerActivityLifecycleCallbacks(activityLifeCallbacks)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
            .also({ appComponent = it })
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return userScopeMonitor.userScopeComponent?.androidInjector ?: super.androidInjector()
    }

    private fun initializeLoggingEnvironment(): CrashReportLogger {
        val logger: CrashReportLogger =
            if (BuildConfig.DEBUG) {
                DebugLogger()
            } else {
                CrashlyticsLogger(this, Crashlytics.getInstance())
            }

        Timber.plant(logger as Timber.Tree)
        return logger
    }
}