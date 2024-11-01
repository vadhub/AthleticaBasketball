package com.vlg.athletica

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.vlg.athletica.data.SaveConfiguration
import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.databinding.ActivityMainBinding
import com.vlg.athletica.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val configuration = SaveConfiguration(this)

    lateinit var bottomMenu: BottomNavigationView
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        bottomMenu = binding.bottomMenu
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomMenu.setupWithNavController(navController)


        if (isAccessInternet(this)) {
            if (configuration.getFirstStart()) {

                bottomMenu.visibility = View.VISIBLE
                RemoteInstance.setUser(
                    User(
                        configuration.getIdUser(),
                        configuration.getNickname(),
                        "",
                        "",
                        configuration.getLogin(),
                        configuration.getPass()
                    )
                )

                Log.d(
                    "$$$",
                    "${configuration.getPass()} ${configuration.getIdUser()}  ${configuration.getNickname()} ${configuration.getLogin()} "
                )
                navController.navigate(R.id.mapFragment)
            }
        } else {
            Snackbar.make(
                bottomMenu,
                resources.getString(R.string.internet_available),
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    fun isAccessInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }
}