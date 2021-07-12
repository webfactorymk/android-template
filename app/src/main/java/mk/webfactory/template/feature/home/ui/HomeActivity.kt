package mk.webfactory.template.feature.home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import mk.webfactory.template.BuildConfig
import mk.webfactory.template.R
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.feature.login.ui.LoginActivity
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserManager
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @set:Inject
    var injectedFragment: HomeFragment? = null

    @Inject
    lateinit var userManager: UserManager<UserSession>

    var userLogoutDisposable: Disposable? = null

    companion object {
        fun startActivity(context: Context, flags: Int? = null) {
            val intent = Intent(context, HomeActivity::class.java)
            flags?.let { intent.addFlags(it) }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (userManager.isLoggedIn().not()) {
            finish()
            return
        }
        setContentView(R.layout.activity_container)
        var homeFragment =
            supportFragmentManager.findFragmentById(R.id.root_container) as HomeFragment?
        if (homeFragment == null) {
            homeFragment = injectedFragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.root_container, homeFragment!!)
            transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (BuildConfig.DEBUG) {
            menu.findItem(R.id.btn_expire_session).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                userLogoutDisposable = userManager.logout { Single.just(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onComplete = {
                            goToLoginActivityAndFinish()
                        },
                        onError = {
                            Timber.e(it)
                            goToLoginActivityAndFinish()
                        }
                    )
                true
            }
            R.id.btn_expire_session -> {
                userManager.updateUser(
                    userManager.getLoggedInUserBlocking()!!.copy(accessToken = null)
                ).blockingGet()
                goToLoginActivityAndFinish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userLogoutDisposable.safeDispose()
    }

    private fun goToLoginActivityAndFinish() {
        LoginActivity.startActivity(this)
        finish()
    }
}