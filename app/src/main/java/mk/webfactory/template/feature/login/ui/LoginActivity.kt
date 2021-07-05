package mk.webfactory.template.feature.login.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import mk.webfactory.template.R
import mk.webfactory.template.data.rx.safeDispose
import mk.webfactory.template.databinding.ActivityLoginBinding
import mk.webfactory.template.feature.home.ui.HomeActivity
import mk.webfactory.template.model.auth.AccessToken
import mk.webfactory.template.model.user.User
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserManager
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object {
        fun startActivityNewTask(context: Context) = startActivity(
            context, FLAG_ACTIVITY_NEW_TASK or
                    FLAG_ACTIVITY_CLEAR_TASK or
                    FLAG_ACTIVITY_CLEAR_TOP
        )

        fun startActivity(context: Context, flags: Int? = null) {
            val intent = Intent(context, LoginActivity::class.java)
            flags?.let { intent.addFlags(it) }
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var userManager: UserManager<UserSession>

    private lateinit var binding: ActivityLoginBinding

    var userLoginDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            userLoginDisposable = userManager.login {
                val userId = binding.editUser.text.ifEmpty { "mock-user-id-16" }.toString()
                Single.just(
                    UserSession(
                        user = User(userId),
                        accessToken = AccessToken("valid-token", "", 10, 11)
                    )
                )
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        HomeActivity.startActivity(this)
                        finish()
                    },
                    onError = { Timber.e(it) }
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userLoginDisposable.safeDispose()
    }
}