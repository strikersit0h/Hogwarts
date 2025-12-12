package com.example.hogwarts_angel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hogwarts_angel.databinding.ActivityProfesorBinding

class ProfesorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfesorBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfesorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarProfesor)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_profesor) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayoutProfesor)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navViewProfesor.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_cerrar_sesion_profesor) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@setNavigationItemSelectedListener true
            }

            val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
            if (handled) {
                binding.drawerLayoutProfesor.closeDrawer(GravityCompat.START)
            }
            handled
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
