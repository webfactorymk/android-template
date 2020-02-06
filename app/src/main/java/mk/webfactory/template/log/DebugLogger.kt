package mk.webfactory.template.log

import android.util.Log
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.User
import timber.log.Timber.DebugTree
import java.util.*

class DebugLogger : DebugTree(), CrashReportLogger {
    override fun setLoggedInUser(user: User?, accessToken: AccessToken?) {
        if (user != null) {
            logSetValue("user_id", user.id)
        }
        if (accessToken != null) {
            logSetValue("access_token", accessToken.token)
        }
    }

    override fun setCurrentPage(page: String) {
        log(Log.INFO, TAG, page, null)
    }

    override fun lastUsedPages(): List<String> {
        return lastUsedPages
    }

    private val lastUsedPages: List<String>
        get() = ArrayList()

    private fun logSetValue(key: String, value: Any) {
        log(Log.INFO, TAG, "$key=$value", null)
    }

    companion object {
        private const val TAG = "CrashReportLogger"
    }
}
