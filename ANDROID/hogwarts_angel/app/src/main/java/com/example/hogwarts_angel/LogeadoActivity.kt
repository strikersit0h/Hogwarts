package com.example.hogwarts_angel

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hogwarts_angel.databinding.ActivityLogeadoBinding
import com.example.hogwarts_angel.viewmodels.LogeadoSharedViewModel

class LogeadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogeadoBinding
    private lateinit var navController: NavController
    // Inicializamos el ViewModel compartido que vivirán en esta Activity
    private val sharedViewModel: LogeadoSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogeadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel.cargarDatosDeSesion()

        setSupportActionBar(binding.toolbarLogeado)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_logeado) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_inicio, R.id.nav_pociones, R.id.nav_hechizos, R.id.nav_perfil
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavView.setupWithNavController(navController)
    }
}
