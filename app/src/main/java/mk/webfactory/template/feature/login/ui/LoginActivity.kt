package mk.webfactory.template.feature.login.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import mk.webfactory.template.R
import mk.webfactory.template.feature.common.ui.BaseActivity

class LoginActivity : BaseActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
    }
}