package com.example.hogwarts_angel
// ... tus imports

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hogwarts_angel.databinding.ActivityMainBinding
import com.example.hogwarts_angel.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Lógica de los Botones ---

        binding.btLogin.setOnClickListener {
            val email = binding.editTextText.text.toString().trim()
            val clave = binding.editTextTextPassword.text.toString()

            if (email.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Debe introducir el email y la contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.iniciarSesion(email, clave)
        }

        binding.btRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // --- Observación del Login ---

        viewModel.usuarioAutenticado.observe(this) { usuario ->
            if (usuario != null) {
                Toast.makeText(this, "Login exitoso. ¡Bienvenido, ${usuario.nombre}!", Toast.LENGTH_LONG).show()

                // Lógica de navegación exitosa

                viewModel.usuarioAutenticado.value = null // Limpiar LiveData

            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                viewModel.error.value = null // Limpiar el LiveData de error
            }
        }
    }
}