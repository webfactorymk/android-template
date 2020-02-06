/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mk.webfactory.template

import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.SilentLogger
import mk.webfactory.template.di.AppComponent
import mk.webfactory.template.di.DaggerAppComponent
import mk.webfactory.template.di.UserScopeMonitor
import mk.webfactory.template.log.CrashReportLogger
import mk.webfactory.template.log.CrashlyticsLogger
import mk.webfactory.template.log.DebugLogger
import mk.webfactory.template.util.ActivityLifeCallbacks
import timber.log.Timber
import java.lang.UnsupportedOperationException
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

    private val activityLifeCallbacks: ActivityLifecycleCallbacks =
        object : ActivityLifeCallbacks() {
            override fun onApplicationEnteredForeground() {
                //TODO: Actions when application enters foreground
            }

            override fun onApplicationEnteredBackground() {}
        }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        crashReportLogger = initializeLoggingEnvironment()
        userScopeMonitor.init()
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
        if (!BuildConfig.DEBUG) {

            //Fixme: use firebase crashlytics
//            val crashlytics = Crashlytics()
//            Fabric.with(
//                Fabric.Builder(this)
//                    .logger(SilentLogger())
//                    .kits(crashlytics)
//                    .build()
//            )
//            val logger = CrashlyticsLogger(this, crashlytics)

            throw UnsupportedOperationException("double init of crashlytics")

//            Timber.plant(logger)
//            return logger
        } else {
            val logger = DebugLogger()
            Timber.plant(logger)
            return logger
        }
    }
}