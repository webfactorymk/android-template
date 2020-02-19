package mk.webfactory.template

import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import mk.webfactory.template.di.AppComponent
import mk.webfactory.template.di.DaggerAppComponent
import mk.webfactory.template.di.UserScopeCreator
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.CrashlyticsLogger
import mk.webfactory.template.log.DebugLogger
import mk.webfactory.template.model.user.User
import mk.webfactory.template.util.ActivityLifeCallbacks
import timber.log.Timber
import javax.inject.Inject

class App : DaggerApplication() {

    companion object {
        val CRASH_REPORT: CrashReportLogger by lazy { crashReportLogger }
        private lateinit var crashReportLogger: CrashReportLogger
    }

    @Inject lateinit var userScopeCreator: UserScopeCreator
    lateinit var appComponent: AppComponent

    private val activityLifeCallbacks = object : ActivityLifeCallbacks() {

        override fun onApplicationEnteredForeground() {
            //TODO: Actions when application enters foreground
        }

        override fun onApplicationEnteredBackground() {}
    }

    private val userScopeMonitorListener = object : UserScopeCreator.Listener {

        override fun onUserScopeCreated(user: User) {
            CRASH_REPORT.setLoggedInUser(user.id)
        }

        override fun onUserScopeDestroyed() {
            CRASH_REPORT.setLoggedInUser("anonymous")
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        crashReportLogger = initializeLoggingEnvironment()
        userScopeCreator.addUserScopeListener(userScopeMonitorListener)
        appComponent.userManager.getLoggedInUserBlocking()?.let { userScopeCreator.createUserScopeComponent(it.user) }
        registerActivityLifecycleCallbacks(activityLifeCallbacks)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
            .also { appComponent = it }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return userScopeCreator.userScopeComponent?.androidInjector ?: super.androidInjector()
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