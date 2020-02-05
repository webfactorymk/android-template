package mk.webfactory.template.di

import io.reactivex.disposables.Disposable
import mk.webfactory.template.App
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.user.User
import mk.webfactory.template.user.UserManager
import mk.webfactory.template.user.UserSession
import javax.inject.Inject

class UserScopeMonitor @Inject constructor(
    private val userManager: UserManager<UserSession>,
    private val app: App
) {

    var userScopeComponent: UserScopeComponent? = null
        private set
    private var userUpdatesDisposable: Disposable? = null


    fun init() {
        if (userManager.isLoggedIn()) {
            createUserScopeComponent(userManager.getLoggedInUser().blockingFirst().user)
        }
        userUpdatesDisposable.safeDispose()
        userUpdatesDisposable = userManager.updates()
            .skip(if (userManager.isUserLoggedIn()) 1 else 0.toLong())
            .subscribe(object : StubObserver<UserSession>() {
                fun onNext(session: UserSession) {
                    if (session.isActive()) {
                        createUserScopeComponent(session)
                    } else {
                        userScopeComponent = null
                    }
                }
            })
    }

    fun destroy() {
        userUpdatesDisposable.safeDispose()
    }

    private fun createUserScopeComponent(user: User) {
        userScopeComponent = app.appComponent.userScopeComponentBuilder()
            .user(user)
            .build()
    }
}