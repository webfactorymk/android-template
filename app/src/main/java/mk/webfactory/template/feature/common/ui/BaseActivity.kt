package mk.webfactory.template.feature.common.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import mk.webfactory.template.App.Companion.CRASH_REPORT
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState?: Bundle())
    }

    override fun onResume() {
        super.onResume()
        CRASH_REPORT.setCurrentPage(javaClass.canonicalName!!)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}