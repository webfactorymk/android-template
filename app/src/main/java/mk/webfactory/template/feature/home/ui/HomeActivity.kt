package mk.webfactory.template.feature.home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import mk.webfactory.template.R
import mk.webfactory.template.feature.common.ui.BaseActivity
import mk.webfactory.template.feature.login.ui.LoginActivity
import javax.inject.Inject


class HomeActivity : BaseActivity() {

    @set:Inject
    var injectedFragment: HomeFragment? = null

    companion object {
        fun startActivity(context: Context, flags: Int? = null) {
            val intent = Intent(context, HomeActivity::class.java)
            flags?.let { intent.addFlags(it) }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        var homeFragment =
            supportFragmentManager.findFragmentById(R.id.root_container) as HomeFragment?
        if (homeFragment == null) {
            homeFragment = injectedFragment
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.replace(R.id.root_container, homeFragment!!)
            transaction.commit()
        }
    }
}