package mk.webfactory.template.feature.launcher.ui

import android.content.Intent
import android.os.Bundle
import mk.webfactory.template.R
import mk.webfactory.template.feature.common.ui.BaseActivity
import mk.webfactory.template.feature.home.ui.HomeActivity
import mk.webfactory.template.model.user.UserSession
import mk.webfactory.template.user.UserManager
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var userManager: UserManager<UserSession>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        startActivity(Intent(this@LauncherActivity, HomeActivity::class.java))
        if (userManager.isLoggedIn()) {
            //startActivity(new Intent(LauncherActivity.this, HomeActivity.class));
            //finish();
        }
    }
}