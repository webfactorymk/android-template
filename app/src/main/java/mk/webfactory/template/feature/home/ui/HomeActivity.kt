package mk.webfactory.template.feature.home.ui

import android.os.Bundle
import mk.webfactory.template.R
import mk.webfactory.template.feature.common.ui.BaseActivity
import javax.inject.Inject


class HomeActivity : BaseActivity() {

    @set:Inject
    var injectedFragment: HomeFragment? = null

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