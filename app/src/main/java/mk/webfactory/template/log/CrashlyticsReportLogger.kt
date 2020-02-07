package mk.webfactory.template.log

import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.User

interface CrashReportLogger {
    fun setLoggedInUser(user: User?, accessToken: AccessToken?)
    fun setCurrentPage(page: String)
    fun lastUsedPages(): List<String>
}
