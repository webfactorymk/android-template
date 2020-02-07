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