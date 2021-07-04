package mk.webfactory.template.log

import android.util.Log
import timber.log.Timber.DebugTree
import java.util.*

class DebugLogger : DebugTree(), CrashReportLogger {

    private val lastUsedPages: List<String>
        get() = ArrayList()

    override fun setLoggedInUser(userIdentifier: String) {
        logSetValue("user_id", userIdentifier)
    }

    override fun setCurrentPage(page: String) {
        log(Log.INFO, TAG, page, null)
    }

    override fun lastUsedPages(): List<String> {
        return lastUsedPages
    }

    private fun logSetValue(key: String, value: Any) {
        log(Log.INFO, TAG, "$key=$value", null)
    }

    companion object {
        private const val TAG = "CrashReportLogger"
    }
}
