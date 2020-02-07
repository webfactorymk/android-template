package mk.webfactory.template.log

interface CrashReportLogger {
    fun setLoggedInUser(userIdentifier: String)
    fun setCurrentPage(page: String)
    fun lastUsedPages(): List<String>
}
