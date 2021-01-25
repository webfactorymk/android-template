package mk.webfactory.template.log

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.util.*

class CrashlyticsLogger(
    private val context: Context,
    crashlytics: FirebaseCrashlytics
) :
    Timber.Tree(), CrashReportLogger {
    private val crashlytics: FirebaseCrashlytics = crashlytics
    private val exceptionHandler: CrashlyticsExceptionHandler
    private val lastUsedPages: ArrayList<String>

    init {
        lastUsedPages = ArrayList(PAGES_LIST_SIZE)
        exceptionHandler = CrashlyticsExceptionHandler(crashlytics, context)
        logDeviceInfo()
    }

    private fun logDeviceInfo() {
        val dm = context.resources.displayMetrics
        crashlytics.setCustomKey("screenResolution", dm.heightPixels.toString() + "x" + dm.widthPixels)
        crashlytics.setCustomKey("screenDensity", dm.density)
        crashlytics.setCustomKey("apiLevel", Build.VERSION.SDK_INT)
        crashlytics.setCustomKey("manufacturer", Build.MANUFACTURER)
        crashlytics.setCustomKey("model", Build.MODEL)
    }

    override fun setLoggedInUser(userIdentifier: String) {
        crashlytics.setUserId(userIdentifier)
    }

    override fun setCurrentPage(page: String) {
        crashlytics.setCustomKey("currentPage", page)
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
        crashlytics.log(message)
        if (t != null) {
            crashlytics.recordException(t)
        }
    }

    companion object {
        private const val PAGES_LIST_SIZE = 5
    }
}