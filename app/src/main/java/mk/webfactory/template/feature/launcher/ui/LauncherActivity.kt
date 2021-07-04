package mk.webfactory.template.feature.launcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import mk.webfactory.template.R
import mk.webfactory.template.feature.home.ui.HomeActivity
import mk.webfactory.template.feature.login.ui.LoginActivity
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserManager
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    //todo use navigation component

    @Inject
    lateinit var userManager: UserManager<UserSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (userManager.isLoggedIn()) {
            HomeActivity.startActivity(this)
        } else {
            LoginActivity.startActivityNewTask(this)
        }
        finish()
    }
}