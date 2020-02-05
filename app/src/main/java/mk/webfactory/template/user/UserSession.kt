package mk.webfactory.template.user

import mk.webfactory.template.model.auth.AccessToken

class UserSession(val user: BaseUser, val accessToken: AccessToken?) {

    /**
     * Check if the session is active (i.e. the user is logged in).
     */
    val isActive: Boolean
        get() = accessToken != null
}