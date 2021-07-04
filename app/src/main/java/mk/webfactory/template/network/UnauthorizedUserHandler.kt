package mk.webfactory.template.network

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import mk.webfactory.template.feature.login.ui.LoginActivity
import timber.log.Timber
import javax.inject.Inject

interface UnauthorizedUserHandler {

    fun onUnauthorizedUserException(exception: UnauthorizedUserException)
}

class UnauthorizedUserHandlerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : UnauthorizedUserHandler {

    override fun onUnauthorizedUserException(exception: UnauthorizedUserException) {
        Timber.e(exception)
        LoginActivity.startActivityNewTask(context)
    }
}