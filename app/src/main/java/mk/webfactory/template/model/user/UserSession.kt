package mk.webfactory.template.model.user

import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.User

class UserSession(val user: User, val accessToken: AccessToken?) {

    /**
     * Check if the session is active (i.e. the user is logged in).
     */
    val isActive: Boolean
        get() = accessToken != null
}