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

package mk.webfactory.template.di

import io.reactivex.disposables.Disposable
import mk.webfactory.template.App
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.model.user.User
import mk.webfactory.template.user.UserManager
import mk.webfactory.template.model.user.UserSession
import javax.inject.Inject

/**
 * Monitors the [UserSession] state and creates and tears down the [UserScopeComponent].
 * Initialize in [App.onCreate] and fetch [userScopeComponent] (nullable) when needed.
 *
 * Example usage with dagger-android:
 * ```
 * class App : DaggerApplication() {
 *
 *     @Inject
 *     lateinit var userScopeMonitor: UserScopeMonitor
 *
 *      override fun onCreate() {
 *          super.onCreate()
 *          userScopeMonitor.init()
 *          // ...
 *      }
 *
 *     override fun androidInjector(): AndroidInjector<Any> {
 *         return userScopeMonitor.userScopeComponent?.androidInjector
 *             ?: super.androidInjector()
 *     }
 *
 *    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
 *        return DaggerAppComponent.builder().application(this).build()
 *            .also({ appComponent = it })
 *    }
 * }
 * ```
 */
class UserScopeMonitor @Inject constructor(
    private val userManager: UserManager<UserSession>,
    private val app: App
) {

    var userScopeComponent: UserScopeComponent? = null
        private set
    private var userUpdatesDisposable: Disposable? = null


    /**
     * Start monitoring user session state. Also checks the last known value.
     */
    fun init() {
        //We need it blocking for the first value if the user is logged in
        // so we can create the component immediately
        if (userManager.isLoggedIn()) {
            createUserScopeComponent(userManager.getLoggedInUserBlocking()!!.user)
        }
        userUpdatesDisposable.safeDispose()
        userUpdatesDisposable = userManager.updates()
            .skip(if (userManager.isLoggedIn()) 1 else 0)
            .distinctUntilChanged { lastState, newState ->
                lastState.isActive == newState.isActive
            }
            .subscribe { session ->
                if (session.isActive) {
                    createUserScopeComponent(session.user)
                } else {
                    userScopeComponent = null
                }
            }
    }

    /**
     * Stops monitoring user's session state and destroys [userScopeComponent].
     */
    fun destroy() {
        userUpdatesDisposable.safeDispose()
        userScopeComponent = null
    }

    private fun createUserScopeComponent(user: User) {
        userScopeComponent = app.appComponent.userScopeComponentBuilder()
            .user(user)
            .build()
    }
}