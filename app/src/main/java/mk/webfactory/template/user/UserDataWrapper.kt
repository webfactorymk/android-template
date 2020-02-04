package mk.webfactory.template.user

import mk.webfactory.template.model.auth.AccessToken

data class UserDataWrapper(val user: User, val accessToken: AccessToken)