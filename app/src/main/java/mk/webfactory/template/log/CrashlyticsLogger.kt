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

package mk.webfactory.template.log

import android.content.Context
import android.os.Build
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.User
import timber.log.Timber
import java.util.*


class CrashlyticsLogger(context: Context, crashlytics: Crashlytics) :
    Timber.Tree(), CrashReportLogger {
    private val context: Context = context
    private val crashlytics: CrashlyticsCore = crashlytics.core
    private val exceptionHandler: CrashlyticsExceptionHandler
    private val lastUsedPages: ArrayList<String>
    private fun logDeviceInfo() {
        val dm = context.resources.displayMetrics
        crashlytics.setString("screenResolution", dm.heightPixels.toString() + "x" + dm.widthPixels)
        crashlytics.setFloat("screenDensity", dm.density)
        crashlytics.setInt("apiLevel", Build.VERSION.SDK_INT)
        crashlytics.setString("manufacturer", Build.MANUFACTURER)
        crashlytics.setString("model", Build.MODEL)
    }

    override fun setLoggedInUser(user: User?, accessToken: AccessToken?) {
        if (user != null) {
            crashlytics.setUserIdentifier(user.id)
        }
        if (accessToken != null) {
            crashlytics.setString("access_token", accessToken.token)
        }
    }

    override fun setCurrentPage(page: String) {
        crashlytics.setString("currentPage", page)
        lastUsedPages.add(page)
        if (lastUsedPages.size > PAGES_LIST_SIZE) {
            lastUsedPages.removeAt(0)
        }
    }

    override fun lastUsedPages(): List<String> {
        return lastUsedPages
    }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        crashlytics.log(priority, tag, message)
        if (t != null) {
            crashlytics.logException(t)
        }
    }

    companion object {
        private const val PAGES_LIST_SIZE = 5
    }

    init {
        lastUsedPages =
            ArrayList(PAGES_LIST_SIZE)
        exceptionHandler =
            CrashlyticsExceptionHandler(crashlytics.core, context)
        logDeviceInfo()
    }
}