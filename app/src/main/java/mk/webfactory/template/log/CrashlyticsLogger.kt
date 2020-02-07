package mk.webfactory.template.log

import android.content.Context
import android.os.Build
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import timber.log.Timber
import java.util.*

class CrashlyticsLogger(
    private val context: Context,
    crashlytics: Crashlytics
) :
    Timber.Tree(), CrashReportLogger {
    private val crashlytics: CrashlyticsCore = crashlytics.core
    private val exceptionHandler: CrashlyticsExceptionHandler
    private val lastUsedPages: ArrayList<String>

    init {
        lastUsedPages = ArrayList(PAGES_LIST_SIZE)
        exceptionHandler = CrashlyticsExceptionHandler(crashlytics.core, context)
        logDeviceInfo()
    }

    private fun logDeviceInfo() {
        val dm = context.resources.displayMetrics
        crashlytics.setString("screenResolution", dm.heightPixels.toString() + "x" + dm.widthPixels)
        crashlytics.setFloat("screenDensity", dm.density)
        crashlytics.setInt("apiLevel", Build.VERSION.SDK_INT)
        crashlytics.setString("manufacturer", Build.MANUFACTURER)
        crashlytics.setString("model", Build.MODEL)
    }

    override fun setLoggedInUser(userIdentifier: String) {
        crashlytics.setUserIdentifier(userIdentifier)
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

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
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
}