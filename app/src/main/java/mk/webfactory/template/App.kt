package mk.webfactory.template

import android.app.Activity
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import mk.webfactory.template.di.AppComponent
import mk.webfactory.template.di.DaggerAppComponent
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.DebugLogger
import timber.log.Timber

class App : DaggerApplication() {

    companion object {
        val CRASH_REPORT: CrashReportLogger by lazy { crashReportLogger }
        private lateinit var crashReportLogger: CrashReportLogger
    }

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        crashReportLogger = initializeLoggingEnvironment()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build().also({ appComponent = it })
    }

    fun activityInjector(): DispatchingAndroidInjector<Activity?> {
        val userScopedActivityInjector: AndroidInjector<Activity?> =
            appComponent.getUserManager().activityInjector()
        return if (userScopedActivityInjector != null) {
            userScopedActivityInjector as DispatchingAndroidInjector<Activity?>
        } else {
            super.activityInjector()
        }
    }

    private fun initializeLoggingEnvironment(): CrashReportLogger {
        if (!BuildConfig.DEBUG) {

            //Fixme: use firebase crashlytics

            throw UnsupportedOperationException("Not imeplemented yet")

            // val crashlytics = Crashlytics()
            // Fabric.with(
            //     Builder(this)
            //         .logger(SilentLogger())
            //         .kits(crashlytics)
            //         .build()
            // )
            // val logger = CrashlyticsLogger(this, crashlytics)
            // Timber.plant(logger)
            // return logger
        } else {
            val logger = DebugLogger()
            Timber.plant(logger)
            return logger
        }
    }
}