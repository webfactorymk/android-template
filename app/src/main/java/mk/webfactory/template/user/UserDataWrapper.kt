package mk.webfactory.template.user

import mk.webfactory.template.model.auth.AccessToken

class UserDataWrapper private constructor(
    val user: User,
    val accessToken: AccessToken
) {

    override fun toString(): String {
        return javaClass.simpleName + "[" +
                "user=" + user + ", " +
                "accessToken=" + accessToken + "]"
    }

    companion object {
        fun from(
            user: User,
            accessToken: AccessToken
        ): UserDataWrapper {
            return UserDataWrapper(user, accessToken)
        }
    }

}