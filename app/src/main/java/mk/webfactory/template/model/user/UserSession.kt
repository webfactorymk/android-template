package mk.webfactory.template.model.user

import mk.webfactory.template.model.auth.AccessToken

data class UserSession(
    val user: User,
    val accessToken: AccessToken?,
    val error: Throwable? = null
) {

    /**
     * Check if the session is active (i.e. the user is logged in).
     */
    val isActive: Boolean
        get() = accessToken != null

    /**
     * Checks if the session has an error.
     */
    val hasError: Boolean
        get() = error != null
}