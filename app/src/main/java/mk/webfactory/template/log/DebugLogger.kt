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
