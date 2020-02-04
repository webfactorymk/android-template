package mk.webfactory.template

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.SilentLogger
import mk.webfactory.androidtemplate.BuildConfig
import mk.webfactory.template.di.AppComponent
import mk.webfactory.template.di.DaggerAppComponent
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.CrashlyticsLogger
import timber.log.Timber

class App : DaggerApplication() {

    companion object {
        lateinit var CRASH_REPORT: CrashReportLogger
    }

    private var appComponent: AppComponent? = null
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initializeLoggingEnvironment()
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

    private fun initializeLoggingEnvironment() {
        if (!BuildConfig.DEBUG) {

            //Fixme: use firebase crashlytics

            val crashlytics = Crashlytics()
            Fabric.with(
                Builder(this)
                    .logger(SilentLogger())
                    .kits(crashlytics)
                    .build()
            )
            val logger = CrashlyticsLogger(this, crashlytics)
            Timber.plant(logger)
            CRASH_REPORT = logger
        } else {
            val logger: mk.webfactory.template.log.DebugLogger =
                mk.webfactory.template.log.DebugLogger()
            Timber.plant(logger)
            CRASH_REPORT = logger
        }
    }
}