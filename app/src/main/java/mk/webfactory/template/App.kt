package mk.webfactory.template

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import mk.webfactory.template.di.AppComponent
import mk.webfactory.template.di.DaggerAppComponent
import mk.webfactory.template.di.UserScopeCreator
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.CrashlyticsLogger
import mk.webfactory.template.log.DebugLogger
import mk.webfactory.template.model.user.User
import mk.webfactory.template.util.ActivityLifeCallbacks
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    companion object {
        val CRASH_REPORT: CrashReportLogger by lazy { crashReportLogger }
        private lateinit var crashReportLogger: CrashReportLogger
    }

    val appComponent: AppComponent by lazy {
        initializeDaggerAppComponent()
    }

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
        RxJavaPlugins.setErrorHandler { Timber.e(it) }
        crashReportLogger = initializeLoggingEnvironment()
        userScopeCreator.addUserScopeListener(userScopeMonitorListener)
        appComponent.userManager.getLoggedInUserBlocking()
            ?.let { userScopeCreator.createUserScopeComponent(it.user) }
        registerActivityLifecycleCallbacks(activityLifeCallbacks)
    }

    private fun initializeDaggerAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
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