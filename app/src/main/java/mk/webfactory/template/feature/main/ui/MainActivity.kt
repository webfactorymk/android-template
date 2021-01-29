package mk.webfactory.template.feature.main.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import mk.webfactory.template.R
import mk.webfactory.template.feature.login.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nav: NavController

    companion object {
        fun startActivityNewTask(context: Context) = startActivity(
            context, Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        )

        fun startActivity(context: Context, flags: Int? = null) {
            val intent = Intent(context, MainActivity::class.java)
            flags?.let { intent.addFlags(it) }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav = Navigation.findNavController(this,R.id.nav_fragment)
    }
}